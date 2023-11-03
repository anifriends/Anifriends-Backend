package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import java.time.LocalDate;
import java.util.List;

public record FindAnimalByShelterResponse(
    Long animalId,
    String name,
    LocalDate birthDate,
    AnimalType type,
    String breed,
    AnimalGender gender,
    boolean isNeutered,
    AnimalActive active,
    double weight,
    String information,
    boolean isAdopted,
    List<String> imageUrls
) {

    public static FindAnimalByShelterResponse from(Animal animal) {
        return new FindAnimalByShelterResponse(
            animal.getAnimalId(),
            animal.getName(),
            animal.getBirthDate(),
            animal.getType(),
            animal.getBreed(),
            animal.getGender(),
            animal.isNeutered(),
            animal.getActive(),
            animal.getWeight(),
            animal.getInformation(),
            animal.isAdopted(),
            animal.getImageUrls()
        );
    }
}
