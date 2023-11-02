package com.clova.anifriends.domain.animal.support.fixture;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDate;
import java.util.List;

public class AnimalFixture {

    public static final String ANIMAL_NAME = "animalName";
    public static final LocalDate ANIMAL_BIRTH_DATE = LocalDate.now();
    public static final String ANIMAL_TYPE = AnimalType.DOG.getValue();
    public static final String ANIMAL_BREED = "animalBreed";
    public static final String ANIMAL_GENDER = AnimalGender.FEMALE.getValue();
    public static final boolean IS_NEUTERED = true;
    public static final String ANIMAL_ACTIVE = AnimalActive.NORMAL.getValue();
    public static final double WEIGHT = 1.2;
    public static final String ANIMAL_INFORMATION = "animalInformation";
    private static final List<String> IMAGE_URLS = List.of("www.aws.s3.com/2", "www.aws.s3.com/2");

    public static Animal animal(Shelter shelter) {
        return new Animal(
            shelter,
            ANIMAL_NAME,
            ANIMAL_BIRTH_DATE,
            ANIMAL_TYPE,
            ANIMAL_BREED,
            ANIMAL_GENDER,
            IS_NEUTERED,
            ANIMAL_ACTIVE,
            WEIGHT,
            ANIMAL_INFORMATION,
            IMAGE_URLS
        );
    }
}
