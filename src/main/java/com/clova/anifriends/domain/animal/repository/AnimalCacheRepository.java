package com.clova.anifriends.domain.animal.repository;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse.FindAnimalResponse;
import com.clova.anifriends.domain.common.PageInfo;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AnimalCacheRepository {

    private static final String ANIMAL_ZSET_KEY = "animal";
    private static final String TOTAL_NUMBER_OF_ANIMALS_KEY = "total_number_of_animals";
    private static final int ANIMAL_CACHE_SIZE = 30;
    private static final long LAST_INDEX = 1L;
    public static final double NANO = 1_000_000_000.0;

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Long> countRedisTemplate;
    private final AnimalRepository animalRepository;

    private ZSetOperations<String, Object> zSetOperations;

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    @Transactional(readOnly = true)
    public void synchronizeCache() {
        zSetOperations.removeRange(ANIMAL_ZSET_KEY, 0, -1);
        Pageable pageable = PageRequest.of(0, ANIMAL_CACHE_SIZE);
        Slice<Animal> animals = animalRepository.findAnimalsByVolunteerV2(null, null, null, null,
            null, null, null, null, pageable);
        animals.forEach(this::saveAnimal);
        long dbCount = animalRepository.countAllAnimalsExceptAdopted();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, dbCount);
    }

    @Transactional(readOnly = true)
    public Long getTotalNumberOfAnimals() {
        Object cachedCount = redisTemplate.opsForValue().get(TOTAL_NUMBER_OF_ANIMALS_KEY);
        if (Objects.nonNull(cachedCount)) {
            return ((Integer) cachedCount).longValue();
        }
        long dbCount = animalRepository.countAllAnimalsExceptAdopted();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, dbCount);
        return dbCount;
    }

    public void saveAnimal(Animal animal) {
        FindAnimalResponse findAnimalResponse = FindAnimalResponse.from(animal);
        zSetOperations.add(ANIMAL_ZSET_KEY, findAnimalResponse, -getScore(animal.getCreatedAt()));
        trimCache();
    }

    public void deleteAnimal(Animal animal) {
        FindAnimalResponse findAnimalResponse = FindAnimalResponse.from(animal);
        zSetOperations.remove(ANIMAL_ZSET_KEY, findAnimalResponse);
    }

    @Transactional(readOnly = true)
    public FindAnimalsResponse findAnimals(int size, long count) {
        if (requiresCacheUpdate(size)) {
            synchronizeCache();
        }
        Set<Object> cachedResponses = zSetOperations.range(ANIMAL_ZSET_KEY, 0,
            size - LAST_INDEX);
        List<FindAnimalResponse> responses = requireNonNull(cachedResponses).stream()
            .map(FindAnimalResponse.class::cast)
            .toList();
        PageInfo pageInfo = PageInfo.of(count, count > size);
        return new FindAnimalsResponse(pageInfo, responses);
    }

    private void trimCache() {
        if (zSetOperations.size(ANIMAL_ZSET_KEY) > ANIMAL_CACHE_SIZE) {
            zSetOperations.removeRange(ANIMAL_ZSET_KEY, LAST_INDEX, LAST_INDEX);
        }
    }

    private boolean requiresCacheUpdate(int size) {
        Long count = zSetOperations.size(ANIMAL_ZSET_KEY);
        return isNull(count) || count < size;
    }

    private double getScore(LocalDateTime createdAt) {
        Instant instant = createdAt.toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond() + instant.getNano() / NANO;
    }

    @Transactional(readOnly = true)
    public void increaseTotalNumberOfAnimals() {
        Long cachedCount = getTotalNumberOfAnimals();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, cachedCount + 1);
    }

    @Transactional(readOnly = true)
    public void decreaseTotalNumberOfAnimals() {
        Long cachedCount = getTotalNumberOfAnimals();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, cachedCount - 1);
    }
}
