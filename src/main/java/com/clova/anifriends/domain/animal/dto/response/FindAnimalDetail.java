package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import java.time.LocalDate;
import java.util.List;

public record FindAnimalDetail(
    Long animalId,
    String animalName,
    LocalDate animalBirthDate,
    AnimalType animalType,
    String animalBreed,
    AnimalGender animalGender,
    boolean animalIsNeutered,
    AnimalActive animalActive,
    double animalWeight,
    String animalInformation,
    List<String> animalImageUrls,
    boolean animalIsAdopted
) {

    public static FindAnimalDetail from(Animal animal) {
        return new FindAnimalDetail(
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
            animal.getImageUrls(),
            animal.isAdopted());
    }

}
