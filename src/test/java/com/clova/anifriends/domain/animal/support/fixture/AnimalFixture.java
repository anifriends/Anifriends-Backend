package com.clova.anifriends.domain.animal.support.fixture;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalAdopted;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.util.ReflectionTestUtils;

public class AnimalFixture {

    public static final String ANIMAL_NAME = "animalName";
    public static final LocalDate ANIMAL_BIRTH_DATE = LocalDate.now();
    public static final String ANIMAL_TYPE = AnimalType.DOG.getName();
    public static final String ANIMAL_BREED = "animalBreed";
    public static final String ANIMAL_GENDER = AnimalGender.FEMALE.getName();
    public static final boolean IS_NEUTERED = true;
    public static final String ANIMAL_ACTIVE = AnimalActive.NORMAL.getName();
    public static final double WEIGHT = 1.2;
    public static final String ANIMAL_INFORMATION = "animalInformation";
    private static final List<String> IMAGE_URLS = List.of("www.aws.s3.com/2", "www.aws.s3.com/3");

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

    public static List<Animal> animals(Shelter shelter, long count) {
        return IntStream.range(0, (int) count)
            .mapToObj(i -> animal(shelter))
            .toList();
    }

    public static Animal animal(Shelter shelter, List<String> imageUrls) {
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
            imageUrls
        );
    }

    public static Animal animal(Shelter shelter, boolean isAdopted) {
        Animal animal = animal(shelter);
        ReflectionTestUtils.setField(animal, "adopted", new AnimalAdopted(isAdopted));
        return animal;
    }
}
