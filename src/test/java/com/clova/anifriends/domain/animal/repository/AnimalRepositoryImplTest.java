package com.clova.anifriends.domain.animal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.support.fixture.AnimalFixture;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

class AnimalRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private AnimalRepositoryImpl customAnimalRepository;

    @Nested
    @DisplayName("findAnimalsByShelter 실행 시")
    class FindAnimalsByShelterTest {

        @Test
        @DisplayName("성공: 이름, 크기가 일치")
        void findAnimalsWhenNameAndSizeAreSame() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);
            Animal animal = AnimalFixture.animal(shelter);
            Animal animalNotFound = new Animal(
                shelter,
                "animalName",
                LocalDate.now().minusMonths(1),
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                14,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );

            PageRequest pageRequest = PageRequest.of(0, 10);

            shelterRepository.save(shelter);
            animalRepository.save(animal);
            animalRepository.save(animalNotFound);

            // when
            Page<Animal> expected = customAnimalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                "animalName",
                null,
                null,
                null,
                null,
                AnimalSize.SMALL,
                null,
                pageRequest
            );

            // then
            assertThat(expected.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공: 이름, 나이가 일치")
        void findAnimalsWhenNameAndAgeAreSame() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            LocalDate animalBirthDate = LocalDate.now().minusMonths(1);
            LocalDate animalNotFoundBirthDate = LocalDate.now().minusMonths(19);

            Animal animal = new Animal(
                shelter,
                "animalName",
                animalBirthDate,
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                4,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );
            Animal animalNotFound = new Animal(
                shelter,
                "animalName",
                animalNotFoundBirthDate,
                AnimalType.DOG.getName(),
                "animalBreed",
                AnimalGender.MALE.getName(),
                true,
                AnimalActive.ACTIVE.getName(),
                14,
                "animalInformation",
                List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
            );
            PageRequest pageRequest = PageRequest.of(0, 10);

            entityManager.persist(shelter);
            entityManager.persist(animal);
            entityManager.persist(animalNotFound);

            // when
            Page<Animal> expected = customAnimalRepository.findAnimalsByShelter(
                shelter.getShelterId(),
                "animalName",
                null,
                null,
                null,
                null,
                null,
                AnimalAge.BABY,
                pageRequest
            );

            // then
            assertThat(expected.getTotalElements()).isEqualTo(1);
        }
    }
}
