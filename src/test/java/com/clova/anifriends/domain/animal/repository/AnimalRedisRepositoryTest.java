package com.clova.anifriends.domain.animal.repository;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animals;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse.FindAnimalResponse;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;

class AnimalRedisRepositoryTest extends BaseIntegrationTest {

    private static final String ANIMAL_ZSET_KEY = "animal:animals";
    private static final int ANIMAL_CACHE_SIZE = 30;

    private ZSetOperations<String, Object> zSetOperations;

    @BeforeEach
    void beforeEach() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    @Test
    @DisplayName("성공: 동시에 총 동물 수 증가")
    void increaseTotalNumberOfAnimals() throws InterruptedException {
        // given
        int count = 10;

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch latch = new CountDownLatch(count);

        // when
        for (int i = 0; i < count; i++) {
            executorService.submit(() -> {
                try {
                    animalRedisRepository.increaseTotalNumberOfAnimals();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertThat(redisTemplate.opsForValue().get("animal:total_number")).isEqualTo(count);
    }

    @Test
    @DisplayName("성공: 동시에 보호 동물 추가")
    void saveAnimal() throws InterruptedException {
        // given
        int count = 50;
        int cacheSize = 30;
        Shelter shelter = shelter();
        List<Animal> animals = animals(shelter, count);
        long id = 1;
        for (Animal animal : animals) {
            ReflectionTestUtils.setField(animal, "animalId", id++);
            ReflectionTestUtils.setField(animal, "createdAt", LocalDateTime.now());
        }

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch latch = new CountDownLatch(count);

        // when
        for (int i = 0; i < count; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    animalRedisRepository.saveAnimal(animals.get(finalI));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        assertThat(redisTemplate.opsForZSet().range("animal:animals", 0, -1))
            .hasSize(cacheSize);
    }

    @Nested
    @DisplayName("synchronizeCache 메서드 실행 시")
    class SynchronizeCacheTest {

        @Test
        @DisplayName("성공: 50개 데이터-> 30개 캐싱")
        void synchronizeCache1() {
            // given
            Shelter shelter = shelter();
            shelterRepository.save(shelter);

            int animalCount = 50;
            List<Animal> animals = animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            int cachedCount = Math.min(animalCount, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalRedisRepository.synchronizeCache();

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
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalRedisRepository.synchronizeCache();

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
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalRedisRepository.synchronizeCache();

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
            animalRedisRepository.synchronizeCache();

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
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            animalRedisRepository.synchronizeCache();

            // when
            animalRedisRepository.saveAnimal(newAnimal);

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
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            animalRedisRepository.synchronizeCache();

            // when
            animalRedisRepository.saveAnimal(newAnimal);

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
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            animalRedisRepository.synchronizeCache();

            // when
            animalRedisRepository.saveAnimal(newAnimal);

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

            animalRedisRepository.synchronizeCache();

            Animal animalToDelete = animals.get(0);
            animalRepository.deleteById(animalToDelete.getAnimalId());

            int cachedCount = Math.min(animalCount - 1, ANIMAL_CACHE_SIZE);
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, cachedCount)).get().map(FindAnimalResponse::from).toList();

            // when
            animalRedisRepository.deleteAnimal(animalToDelete);

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

            animalRedisRepository.synchronizeCache();

            Animal animalToDelete = animals.get(0);
            animalRepository.deleteById(animalToDelete.getAnimalId());

            int cachedCount = Math.min(animalCount - 1, ANIMAL_CACHE_SIZE);
            // when
            animalRedisRepository.deleteAnimal(animalToDelete);

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

            animalRedisRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalRedisRepository.findAnimals(size, animalCount)
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

            animalRedisRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalRedisRepository.findAnimals(size, animalCount)
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

            animalRedisRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalRedisRepository.findAnimals(size, animalCount)
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

            animalRedisRepository.synchronizeCache();

            int size = 20;
            List<FindAnimalResponse> expected = animalRepository.findAnimalsV2(null,
                null, null, null, null, null, null,
                null, PageRequest.of(0, size)).get().map(FindAnimalResponse::from).toList();

            // when
            List<FindAnimalResponse> result = animalRedisRepository.findAnimals(size, animalCount)
                .animals();

            // then
            assertThat(result).hasSize(Math.min(size, animalCount));
            assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
        }
    }

}