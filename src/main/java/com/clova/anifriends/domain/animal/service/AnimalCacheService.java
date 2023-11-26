package com.clova.anifriends.domain.animal.service;

import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimalCacheService {

    private final AnimalRepository animalRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private static final String TOTAL_NUMBER_OF_ANIMALS_KEY = "total_number_of_animals";

    public Long getTotalNumberOfAnimals() {
        Long cachedCount = redisTemplate.opsForValue().get(TOTAL_NUMBER_OF_ANIMALS_KEY);
        if (cachedCount != null) {
            return cachedCount;
        }
        long dbCount = animalRepository.countAllAnimalsExceptAdopted();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, dbCount);
        return dbCount;
    }

    public void increaseTotalNumberOfAnimals() {
        Long cachedCount = getTotalNumberOfAnimals();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, cachedCount + 1);
    }

    public void decreaseTotalNumberOfAnimals() {
        Long cachedCount = getTotalNumberOfAnimals();
        redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, cachedCount - 1);
    }
}
