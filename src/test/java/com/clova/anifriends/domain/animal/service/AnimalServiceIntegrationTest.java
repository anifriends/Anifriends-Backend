package com.clova.anifriends.domain.animal.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AnimalServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AnimalService animalService;

    @Autowired
    ShelterRepository shelterRepository;

    @Autowired
    AnimalRepository animalRepository;

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
            assertThat(animal.getImageUrls()).hasSize(imageUrls.size());
        }
    }
}
