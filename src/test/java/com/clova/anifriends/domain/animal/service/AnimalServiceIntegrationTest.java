package com.clova.anifriends.domain.animal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalImage;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse.FindAnimalResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.repository.AnimalRedisRepository;
import com.clova.anifriends.domain.animal.repository.response.FindAnimalsResult;
import com.clova.anifriends.domain.animal.support.fixture.AnimalDtoFixture;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

public class AnimalServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AnimalService animalService;

    @Autowired
    AnimalRedisRepository animalRedisRepository;

    @Nested
    @DisplayName("registerAnimal 메서드 실행 시")
    class RegisterAnimalTest {

        List<String> imageUrls = List.of("www.aws.s3.com/2", "www.aws.s3.com/3");
        RegisterAnimalRequest registerAnimalRequest = new RegisterAnimalRequest(
            "name",
            LocalDate.now(),
            AnimalType.DOG.getName(),
            "품종",
            AnimalGender.FEMALE.getName(),
            false,
            AnimalActive.QUIET.getName(),
            0.7,
            "기타 정보",
            imageUrls
        );

        @Test
        @DisplayName("성공")
        void registerAnimal() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            //when
            RegisterAnimalResponse response = animalService.registerAnimal(
                shelter.getShelterId(), registerAnimalRequest);

            //then
            Animal animal = entityManager.createQuery(
                    "select a from Animal a"
                        + " join fetch a.images"
                        + " where a.animalId = :id",
                    Animal.class)
                .setParameter("id", response.animalId())
                .getSingleResult();
            assertThat(animal.getImages()).hasSize(imageUrls.size());
        }
    }

    @Nested
    @DisplayName("deleteAnimal 메서드 호출 시")
    class DeleteAnimalTest {

        @Test
        @DisplayName("성공: 보호 동물 이미지도 함께 삭제 됨")
        void deleteAnimalWithAnimalImages() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            Animal animal = AnimalFixture.animal(shelter);
            List<String> imageUrls = List.of("image1", "image2", "image3");

            animal.updateAnimal(
                animal.getName(),
                animal.getBirthDate(),
                animal.getType(),
                animal.getBreed(),
                animal.getGender(),
                animal.isNeutered(),
                animal.getActive(),
                animal.getWeight(),
                animal.getInformation(),
                imageUrls
            );
            shelterRepository.save(shelter);
            animalRepository.save(animal);

            //when
            animalService.deleteAnimal(shelter.getShelterId(), animal.getAnimalId());

            //then
            verify(s3Service, times(1)).deleteImages(imageUrls);
            Animal findAnimal = entityManager.find(Animal.class, animal.getAnimalId());
            assertThat(findAnimal).isNull();
            List<AnimalImage> findAnimalImages = entityManager.createQuery(
                    "select ai from AnimalImage ai", AnimalImage.class)
                .getResultList();
            assertThat(findAnimalImages).isEmpty();
        }
    }

    @Nested
    @Transactional
    @DisplayName("findAnimalsByVolunteerV2 실행 시 (첫 페이지 캐싱 테스트)")
    class FindAnimalsByVolunteerV2Test {

        @Test
        @DisplayName("성공: 30개 데이터, 30개 캐싱, 20개 조회 -> 20개 조회")
        void findAnimalsByVolunteer1() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            long animalCount = 30;
            int size = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            Slice<FindAnimalsResult> pagination = animalRepository.findAnimalsV2(null,
                null, null, null, null, null,
                null, null, PageRequest.of(0, size));
            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pagination,
                animalCount);

            animalRedisRepository.synchronizeCache();

            // when
            FindAnimalsResponse result = animalService.findAnimalsV2(
                null, null, null, null, null, null,
                null, null, PageRequest.of(0, size));

            // then
            assertThat(result.animals()).hasSize(Math.min(size, (int) animalCount));
            assertThat(result.pageInfo()).isEqualTo(expected.pageInfo());
            assertThat(result.animals()).containsExactlyInAnyOrderElementsOf(expected.animals());

        }

        @Test
        @DisplayName("성공: 10개 데이터, 10개 캐싱, 20개 조회 -> 10개 조회")
        void findAnimalsByVolunteer2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            long animalCount = 10;
            int size = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            Slice<FindAnimalsResult> pagination = animalRepository.findAnimalsV2(null,
                null, null, null, null, null,
                null, null, PageRequest.of(0, size));
            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pagination,
                animalCount);

            animalRedisRepository.synchronizeCache();

            // when
            FindAnimalsResponse result = animalService.findAnimalsV2(
                null, null, null, null, null, null,
                null, null, PageRequest.of(0, size));

            // then
            assertThat(result.animals()).hasSize(Math.min(size, (int) animalCount));
            assertThat(result.pageInfo()).isEqualTo(expected.pageInfo());
            assertThat(result.animals()).containsExactlyInAnyOrderElementsOf(expected.animals());

        }

        @Test
        @DisplayName("성공: 30개 데이터, 20개 캐싱, 1개 삭제, 20개 조회 -> 20개 조회")
        void findAnimalsByVolunteer3() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            long animalCount = 30;
            int size = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalRedisRepository.synchronizeCache();

            Animal animalToDelete = animals.get((int) animalCount - 1);
            FindAnimalResponse responseToDelete = FindAnimalResponse.from(animalToDelete);

            animalService.deleteAnimal(shelter.getShelterId(), animalToDelete.getAnimalId());

            Slice<FindAnimalsResult> pagination = animalRepository.findAnimalsV2(null,
                null, null, null, null, null,
                null, null, PageRequest.of(0, size));
            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pagination,
                animalCount - 1);

            // when
            FindAnimalsResponse result = animalService.findAnimalsV2(
                null, null, null, null, null, null,
                null, null, PageRequest.of(0, size));

            // then
            assertThat(result.animals()).hasSize(Math.min(size, (int) animalCount));
            assertThat(result.pageInfo()).isEqualTo(expected.pageInfo());
            assertThat(result.animals()).containsExactlyInAnyOrderElementsOf(expected.animals());
            assertThat(result.animals()).doesNotContain(responseToDelete);

        }

        @Test
        @DisplayName("성공: 20개 데이터, 20개 캐싱, 1개 삭제, 20개 조회 -> 19개 조회")
        void findAnimalsByVolunteer4() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            long animalCount = 20;
            int size = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalRedisRepository.synchronizeCache();

            Animal animalToDelete = animals.get((int) animalCount - 1);
            FindAnimalResponse responseToDelete = FindAnimalResponse.from(animalToDelete);

            animalService.deleteAnimal(shelter.getShelterId(), animalToDelete.getAnimalId());

            Slice<FindAnimalsResult> pagination = animalRepository.findAnimalsV2(null,
                null, null, null, null, null,
                null, null, PageRequest.of(0, size));
            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pagination,
                animalCount - 1);

            // when
            FindAnimalsResponse result = animalService.findAnimalsV2(
                null, null, null, null, null, null,
                null, null, PageRequest.of(0, size));

            // then
            assertThat(result.animals()).hasSize(Math.min(size, (int) animalCount - 1));
            assertThat(result.pageInfo()).isEqualTo(expected.pageInfo());
            assertThat(result.animals()).containsExactlyInAnyOrderElementsOf(expected.animals());
            assertThat(result.animals()).doesNotContain(responseToDelete);
        }

        @Test
        @DisplayName("성공: 19개 데이터, 19개 캐싱, 1개 추가, 20개 조회 -> 20개 조회")
        void findAnimalsByVolunteer5() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            shelterRepository.save(shelter);

            long animalCount = 19;
            int size = 20;
            List<Animal> animals = AnimalFixture.animals(shelter, animalCount);
            animalRepository.saveAll(animals);

            animalRedisRepository.synchronizeCache();

            Animal animalToAdd = AnimalFixture.animal(shelter);

            animalService.registerAnimal(shelter.getShelterId(),
                AnimalDtoFixture.registerAnimal(animalToAdd));

            Slice<FindAnimalsResult> pagination = animalRepository.findAnimalsV2(null,
                null, null, null, null, null,
                null, null, PageRequest.of(0, size));
            FindAnimalsResponse expected = FindAnimalsResponse.fromV2(pagination,
                animalCount + 1);

            // when
            FindAnimalsResponse result = animalService.findAnimalsV2(
                null, null, null, null, null, null,
                null, null, PageRequest.of(0, size));

            // then
            assertThat(result.animals()).hasSize(Math.min(size, (int) animalCount + 1));
            assertThat(result.pageInfo()).isEqualTo(expected.pageInfo());
            assertThat(result.animals()).containsExactlyInAnyOrderElementsOf(expected.animals());
        }
    }

    @Nested
    @DisplayName("findAnimals 메서드 실행 시")
    class FindAnimalsTest {

        AnimalType animalType = null;
        AnimalActive animalActive = null;
        AnimalNeuteredFilter animalNeuteredFilter = null;
        AnimalAge animalAge = null;
        AnimalGender animalGender = null;
        AnimalSize animalSize = null;

        @Test
        @DisplayName("성공")
        void findAnimals() {
            //given
            Shelter shelter = ShelterFixture.shelter();
            Animal animal = AnimalFixture.animal(shelter);
            Animal animalB = AnimalFixture.animal(shelter);
            shelterRepository.save(shelter);
            animalRepository.save(animal);
            animalRepository.save(animalB);
            PageRequest pageRequest = PageRequest.of(0, 20);

            //when
            FindAnimalsResponse findAnimals = animalService.findAnimals(animalType, animalActive,
                animalNeuteredFilter, animalAge, animalGender, animalSize, pageRequest);

            //then
            assertThat(findAnimals.animals()).hasSize(2);
        }
    }
}
