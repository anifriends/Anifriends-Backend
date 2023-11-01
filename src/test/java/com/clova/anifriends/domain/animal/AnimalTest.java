package com.clova.anifriends.domain.animal;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AnimalTest {

    @Nested
    @DisplayName("Animal 생성 시")
    class NewAnimalTest {

        @Test
        @DisplayName("성공")
        void newAnimal() {
            //given
            Shelter shelter = new Shelter(
                "eamil@email.com", "password123!", "address", "address",
                "name", "02-1234-5678", "02-1234-5678", false);
            String name = "animal";
            LocalDate birthDate = LocalDate.now();
            String type = AnimalType.CAT.getName();
            String breed = "breed";
            String gender = AnimalGender.MALE.getName();
            boolean isNeutered = false;
            String active = AnimalActive.ACTIVE.getName();
            double weight = 0.7;
            String information = "info";

            //when
            Animal animal = new Animal(
                shelter, name, birthDate, type, breed, gender, isNeutered, active, weight,
                information);

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
    }
}