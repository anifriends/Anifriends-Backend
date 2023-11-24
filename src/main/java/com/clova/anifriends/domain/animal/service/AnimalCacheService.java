package com.clova.anifriends.domain.animal.service;

import com.clova.anifriends.domain.animal.dto.response.FindAnimalRedisDto;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalCacheService {

    public static final String WILD_CARD = "*";
    private final RedisTemplate<String, FindAnimalRedisDto> redisTemplate;
    private final AnimalRepository animalRepository;
    private static final String ANIMAL_BASE_KEY = "ANIMAL:";
    private static final String ANIMAL_COUNT_KEY = "ANIMAL:COUNT";
    private static final int ANIMAL_MAX_SIZE = 100;

    public void saveAnimal(FindAnimalRedisDto findAnimalRedisDto) {
        if (!isCachedRight()) {
            synchronizeAnimals();
        }
        ZSetOperations<String, FindAnimalRedisDto> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(
            ANIMAL_BASE_KEY + findAnimalRedisDto.animalId(),
            findAnimalRedisDto,
            findAnimalRedisDto.createdAt()
                .toEpochSecond(java.time.OffsetDateTime.now().getOffset()));
        if (zSetOperations.size(ANIMAL_BASE_KEY + WILD_CARD) > ANIMAL_MAX_SIZE) {
            zSetOperations.popMax(ANIMAL_BASE_KEY + "*");
        }
    }

//    public void deleteAnimal(Long animalId) {
//        ZSetOperations<String, FindAnimalRedisDto> zSetOperations = redisTemplate.opsForZSet();
//        synchronizeAnimals();
//
//        Set<FindAnimalRedisDto> lastAnimal = zSetOperations.range(ANIMAL_BASE_KEY + WILD_CARD, -1, -1).;
////        zSetOperations.remove(ANIMAL_BASE_KEY + WILD_CARD, animalId);
//    }

    public List<FindAnimalRedisDto> findAnimals(int size) {
        if (!isCachedRight()) {
            synchronizeAnimals();
        }
        ZSetOperations<String, FindAnimalRedisDto> zSetOperations = redisTemplate.opsForZSet();
        Set<FindAnimalRedisDto> result = zSetOperations.reverseRange(ANIMAL_BASE_KEY + WILD_CARD, 0,
            size - 1L);
        return new ArrayList<>(result);
    }

    private void synchronizeAnimals() {
        Pageable pageable = PageRequest.of(0, ANIMAL_MAX_SIZE);
        ZSetOperations<String, FindAnimalRedisDto> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.removeRange(ANIMAL_BASE_KEY + WILD_CARD, 0, -1);
        animalRepository.findAnimalOrderByCreatedAtDescWithLimit(pageable)
            .stream()
            .map(FindAnimalRedisDto::from)
            .forEach(this::saveAnimal);
    }

    private boolean isCachedRight() {
        ZSetOperations<String, FindAnimalRedisDto> zSetOperations = redisTemplate.opsForZSet();
        Long count = zSetOperations.zCard(ANIMAL_COUNT_KEY + WILD_CARD);
        return Objects.nonNull(count) && count > 0;
    }
}
