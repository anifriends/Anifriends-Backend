package com.clova.anifriends.domain.animal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
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

class AnimalTest {

    @Nested
    @DisplayName("Animal 생성 시")
    class NewAnimalTest {

        Shelter shelter = ShelterFixture.shelter();
        String name = "animal";
        LocalDate birthDate = LocalDate.now();
        String type = AnimalType.CAT.getName();
        String breed = "breed";
        String gender = AnimalGender.MALE.getName();
        boolean isNeutered = false;
        String active = AnimalActive.ACTIVE.getName();
        double weight = 0.7;
        String information = "info";
        List<String> imageUrls = List.of("www.aws.s3.com/2", "www.aws.s3.com/3");

        @Test
        @DisplayName("성공")
        void newAnimal() {
            //given
            //when
            Animal animal = new Animal(
                shelter, name, birthDate, type, breed, gender, isNeutered, active, weight,
                information, imageUrls);

            //then
            assertThat(animal.getShelter()).isEqualTo(shelter);
            assertThat(animal.getName()).isEqualTo(name);
            assertThat(animal.getBirthDate()).isEqualTo(birthDate);
            assertThat(animal.getType()).isEqualTo(AnimalType.valueOf(type));
            assertThat(animal.getBreed()).isEqualTo(breed);
            assertThat(animal.getGender()).isEqualTo(AnimalGender.valueOf(gender));
            assertThat(animal.isNeutered()).isEqualTo(isNeutered);
            assertThat(animal.getActive()).isEqualTo(AnimalActive.valueOf(active));
            assertThat(animal.getWeight()).isEqualTo(weight);
            assertThat(animal.getInformation()).isEqualTo(information);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 보호 동물 이미지가 null")
        void exceptionWhenImageIsNull() {
            //given
            List<String> nullImageUrls = null;

            //when
            Exception exception = catchException(
                () -> new Animal(shelter, name, birthDate, type, breed, gender, isNeutered, active,
                    weight, information, nullImageUrls));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 보호 동물 이미지가 1장 이하")
        void exceptionWhenImageUrlsSizeIsZero() {
            //given
            List<String> imageUrlsEmpty = List.of();

            //when
            Exception exception = catchException(
                () -> new Animal(shelter, name, birthDate, type, breed, gender, isNeutered, active,
                    weight, information, imageUrlsEmpty));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }

        @Test
        @DisplayName("예외(AnimalBadRequestException): 보호 동물 이미지가 5장 이상")
        void exceptionWhenImageUrlsSizeOver5() {
            //given
            List<String> imageUrlsOver5 = List.of("www.aws.s3.com/2", "www.aws.s3.com/2",
                "www.aws.s3.com/2", "www.aws.s3.com/2", "www.aws.s3.com/2", "www.aws.s3.com/2");

            //when
            Exception exception = catchException(
                () -> new Animal(shelter, name, birthDate, type, breed, gender, isNeutered, active,
                    weight, information, imageUrlsOver5));

            //then
            assertThat(exception).isInstanceOf(AnimalBadRequestException.class);
        }
    }
}
