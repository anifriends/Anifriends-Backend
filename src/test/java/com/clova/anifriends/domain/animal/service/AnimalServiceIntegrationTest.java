package com.clova.anifriends.domain.animal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalImage;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.global.image.S3Service;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AnimalServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AnimalService animalService;

    @Autowired
    ShelterRepository shelterRepository;

    @Autowired
    AnimalRepository animalRepository;

    @MockBean
    S3Service s3Service;

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
}
