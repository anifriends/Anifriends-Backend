
package com.clova.anifriends.domain.animal.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse.FindAnimalResponse;
import com.clova.anifriends.domain.animal.repository.AnimalCacheRepository;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
class AnimalCacheServiceTest extends BaseIntegrationTest {

    private static final String ANIMAL_ZSET_KEY = "animal";
    private static final int ANIMAL_CACHE_SIZE = 30;

    @Autowired
    private AnimalCacheRepository animalCacheRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;

    @BeforeEach
    void tearDown() {
        zSetOperations = redisTemplate.opsForZSet();
        redisTemplate.delete(ANIMAL_ZSET_KEY);
    }

    @Nested
    @DisplayName("synchronizeCache 메서드 실행 시")
    class SynchronizeCacheTest {

        @Test
        @DisplayName("성공: 50개 데이터-> 30개 캐싱")
        void synchronizeCache1() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 50;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            int cachedCount = Math.min(animalCount, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalCacheRepository.synchronizeCache();

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 30개 데이터 -> 30개 캐싱")
        void synchronizeCache2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 30;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            int cachedCount = Math.min(animalCount, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalCacheRepository.synchronizeCache();

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 20개 데이터 -> 20개 캐싱")
        void synchronizeCache3() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            int cachedCount = Math.min(animalCount, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalCacheRepository.synchronizeCache();

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 0개 데이터 -> 0개 캐싱")
        void synchronizeCache4() {
            // given
            int animalCount = 0;
            int cachedCount = Math.min(animalCount, ANIMAL_CACHE_SIZE);

            // when
            animalCacheRepository.synchronizeCache();

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
        }
    }

    @Nested
    @DisplayName("saveAnimal 메서드 실행 시")
    class SaveAnimalTest {

        @Test
        @DisplayName("성공: 30개 캐싱 -> 30개 캐싱")
        void saveAnimal1() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 30;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            Animal newAnimal = AnimalFixture.animal(shelter);
            animalRepository.save(newAnimal);

            int cachedCount = Math.min(animalCount + 1, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            animalCacheRepository.synchronizeCache();

            // when
            animalCacheRepository.saveAnimal(newAnimal);

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 29개 캐싱 -> 30개 캐싱")
        void saveAnimal2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 29;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            Animal newAnimal = AnimalFixture.animal(shelter);
            animalRepository.save(newAnimal);

            int cachedCount = Math.min(animalCount + 1, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            animalCacheRepository.synchronizeCache();

            // when
            animalCacheRepository.saveAnimal(newAnimal);

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 0개 캐싱 -> 1개 캐싱")
        void saveAnimal3() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 0;

            Animal newAnimal = AnimalFixture.animal(shelter);
            animalRepository.save(newAnimal);

            int cachedCount = Math.min(animalCount + 1, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            animalCacheRepository.synchronizeCache();

            // when
            animalCacheRepository.saveAnimal(newAnimal);

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    @Nested
    @DisplayName("deleteAnimal 메서드 실행 시")
    class DeleteAnimal {

        @Test
        @DisplayName("성공: 30개 캐싱 -> 29개 캐싱")
        void deleteAnimal1() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 30;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalCacheRepository.synchronizeCache();

            Animal animalToDelete = animals.get(0);
            animalRepository.deleteById(animalToDelete.getAnimalId());

            int cachedCount = Math.min(animalCount - 1, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalCacheRepository.deleteAnimal(animalToDelete);

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
            assertThat(zSetOperations.range(ANIMAL_ZSET_KEY, 0, -1))
                .extracting(obj -> (FindAnimalResponse) obj)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 1개 캐싱 -> 0개 캐싱")
        void deleteAnimal2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 1;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalCacheRepository.synchronizeCache();

            Animal animalToDelete = animals.get(0);
            animalRepository.deleteById(animalToDelete.getAnimalId());

            int cachedCount = Math.min(animalCount - 1, ANIMAL_CACHE_SIZE);
            // when
            animalCacheRepository.deleteAnimal(animalToDelete);

            // then
            assertThat(zSetOperations.size(ANIMAL_ZSET_KEY)).isEqualTo(cachedCount);
        }
    }

    @Nested
    @DisplayName("findAnimals 메서드 실행 시")
    class FindAnimalsTest {

        @Test
        @DisplayName("성공: 30개 캐싱, animalSize 20 -> 20개 반환")
        void findAnimals1() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 30;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalCacheRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalCacheRepository.findAnimals(size, animalCount)
                .animals();

            // then
            assertThat(result).hasSize(Math.min(size, animalCount));
            assertThat(result)
                .containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 20개 캐싱, animalSize 20 -> 20개 반환")
        void findAnimals2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalCacheRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalCacheRepository.findAnimals(size, animalCount)
                .animals();

            // then
            assertThat(result).hasSize(Math.min(size, animalCount));
            assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 10개 캐싱, animalSize 20 -> 10개 반환")
        void findAnimals3() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            int animalCount = 10;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalCacheRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalCacheRepository.findAnimals(size, animalCount)
                .animals();

            // then
            assertThat(result).hasSize(Math.min(size, animalCount));
            assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        @DisplayName("성공: 0개 캐싱, animalSize 20 -> 0개 반환")
        void findAnimals4() {
            // given
            int animalCount = 0;

            animalCacheRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsByVolunteerV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalCacheRepository.findAnimals(size, animalCount)
                .animals();

            // then
            assertThat(result).hasSize(Math.min(size, animalCount));
            assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
        }
    }
}
