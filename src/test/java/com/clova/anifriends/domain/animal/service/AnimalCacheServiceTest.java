package com.clova.anifriends.domain.animal.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
class AnimalCacheServiceTest extends BaseIntegrationTest {

    private static final String TOTAL_NUMBER_OF_ANIMALS_KEY = "total_number_of_animals";

    @Autowired
    private AnimalCacheService animalCacheService;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    private Shelter shelter;
    private Animal animal1;
    private Animal animal2;
    private Animal animal3;

    @BeforeEach
    void setUp() {
        redisTemplate.delete(TOTAL_NUMBER_OF_ANIMALS_KEY);
        shelter = ShelterFixture.shelter();
        shelterRepository.save(shelter);
        animal1 = AnimalFixture.animal(shelter);
        animal2 = AnimalFixture.animal(shelter);
        animal3 = AnimalFixture.animal(shelter, true);
        animalRepository.saveAll(Arrays.asList(animal1, animal2, animal3));
    }

    @Nested
    @DisplayName("getTotalNumberOfAnimals 메서드 실행 시")
    class GetTotalNumberOfAnimalsTest {

        @Test
        @DisplayName("성공: 전체 동물 수를 redis에서 가져온다.")
        void getTotalNumberOfAnimalsByRedis() {
            // given
            redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, 4L);

            // when
            long count = animalCacheService.getTotalNumberOfAnimals();

            // then
            assertThat(count).isEqualTo(4L);
        }

        @Test
        @DisplayName("성공: 전체 동물 수 캐시가 null일 경우 db에서 가져온다.")
        void getTotalNumberOfAnimalsWhenCacheIsNull() {
            // given

            // when
            long count = animalCacheService.getTotalNumberOfAnimals();

            // then
            assertThat(count).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("increaseTotalNumberOfAnimals 메서드 실행 시")
    class IncreaseTotalNumberOfAnimalsTest {

        @Test
        @DisplayName("성공: 전체 동물 수를 1 증가시킨다.")
        void increaseTotalNumberOfAnimals() {
            // given
            redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, 4L);

            // when
            animalCacheService.increaseTotalNumberOfAnimals();
            long count = animalCacheService.getTotalNumberOfAnimals();

            // then
            assertThat(count).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("decreaseTotalNumberOfAnimals 메서드 실행 시")
    class DecreaseTotalNumberOfAnimalsTest {

        @Test
        @DisplayName("성공: 전체 동물 수를 1 감소시킨다.")
        void decreaseTotalNumberOfAnimals() {
            // given
            redisTemplate.opsForValue().set(TOTAL_NUMBER_OF_ANIMALS_KEY, 4L);

            // when
            animalCacheService.decreaseTotalNumberOfAnimals();
            long count = animalCacheService.getTotalNumberOfAnimals();

            // then
            assertThat(count).isEqualTo(3L);
        }
    }
}
