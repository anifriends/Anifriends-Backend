package com.clova.anifriends.domain.animal.repository;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse.FindAnimalResponse;
import com.clova.anifriends.domain.animal.repository.response.FindAnimalsResult;
import com.clova.anifriends.domain.common.PageInfo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalRedisRepository implements AnimalCacheRepository {

    private static final String ANIMAL_ZSET_KEY = "animal:animals";
    private static final String TOTAL_NUMBER_OF_ANIMALS_KEY = "animal:total_number";
    private static final int ANIMAL_CACHE_SIZE = 30;
    private static final long LAST_INDEX = -1;
    public static final double NANO = 1_000_000_000.0;

    private final AnimalRepository animalRepository;

    private final ZSetOperations<String, Object> zSetOperations;
    private final ValueOperations<String, Object> valueOperations;


    public AnimalRedisRepository(
        RedisTemplate<String, Object> redisTemplate,
        AnimalRepository animalRepository
    ) {
        this.animalRepository = animalRepository;
        this.zSetOperations = redisTemplate.opsForZSet();
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void synchronizeCache() {
        zSetOperations.removeRange(ANIMAL_ZSET_KEY, 0, LAST_INDEX);

        Slice<FindAnimalsResult> animals = getFindAnimalsResults();
        animals.forEach(this::saveAnimal);

        long dbCount = animalRepository.countAllAnimalsExceptAdopted();
        valueOperations.set(TOTAL_NUMBER_OF_ANIMALS_KEY, dbCount);
    }

    @Override
    public Long getTotalNumberOfAnimals() {
        Object cachedCount = valueOperations.get(TOTAL_NUMBER_OF_ANIMALS_KEY);
        if (Objects.nonNull(cachedCount)) {
            return ((Integer) cachedCount).longValue();
        }
        long dbCount = animalRepository.countAllAnimalsExceptAdopted();
        valueOperations.set(TOTAL_NUMBER_OF_ANIMALS_KEY, dbCount);
        return dbCount;
    }

    @Override
    public FindAnimalsResponse findAnimals(int size, long count) {
        Set<Object> cachedResponses = zSetOperations.range(ANIMAL_ZSET_KEY, 0, size - 1L);
        PageInfo pageInfo = PageInfo.of(count, count > size);

        if (cachedResponses.size() == size) {
            List<FindAnimalResponse> responses = requireNonNull(cachedResponses).stream()
                .map(FindAnimalResponse.class::cast)
                .toList();
            return new FindAnimalsResponse(pageInfo, responses);
        }

        zSetOperations.removeRange(ANIMAL_ZSET_KEY, 0, LAST_INDEX);

        Slice<FindAnimalsResult> animalsResult = getFindAnimalsResults();
        animalsResult.forEach(this::saveAnimal);

        List<FindAnimalResponse> responses = animalsResult.stream()
            .map(FindAnimalResponse::from)
            .toList();
        return new FindAnimalsResponse(pageInfo, responses);
    }

    @Override
    public void saveAnimal(FindAnimalsResult animal) {
        FindAnimalResponse findAnimalResponse = FindAnimalResponse.from(animal);
        zSetOperations.add(ANIMAL_ZSET_KEY, findAnimalResponse, -getScore(animal.getCreatedAt()));
        trimCache();
    }

    @Override
    public void saveAnimal(Animal animal) {
        FindAnimalResponse findAnimalResponse = FindAnimalResponse.from(animal);
        zSetOperations.add(ANIMAL_ZSET_KEY, findAnimalResponse, -getScore(animal.getCreatedAt()));
        trimCache();
    }

    @Override
    public long deleteAnimal(Animal animal) {
        FindAnimalResponse findAnimalResponse = FindAnimalResponse.from(animal);
        Long number = zSetOperations.remove(ANIMAL_ZSET_KEY, findAnimalResponse);
        return isNull(number) ? 0 : number;
    }

    private void trimCache() {
        zSetOperations.removeRange(ANIMAL_ZSET_KEY, ANIMAL_CACHE_SIZE, LAST_INDEX);
    }

    private Slice<FindAnimalsResult> getFindAnimalsResults() {
        Pageable pageable = PageRequest.of(0, ANIMAL_CACHE_SIZE);
        return animalRepository.findAnimalsV2(null, null, null,
            null, null, null, null, null, pageable);
    }

    private double getScore(LocalDateTime createdAt) {
        Instant instant = createdAt.toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond() + instant.getNano() / NANO;
    }

    @Override
    public void increaseTotalNumberOfAnimals() {
        valueOperations.increment(TOTAL_NUMBER_OF_ANIMALS_KEY);
    }

    @Override
    public void decreaseTotalNumberOfAnimals() {
        valueOperations.decrement(TOTAL_NUMBER_OF_ANIMALS_KEY);
    }
}
