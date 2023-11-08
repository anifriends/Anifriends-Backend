package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import java.time.LocalDate;
import java.util.List;

public record FindAnimalDetail(
    String name,
    LocalDate birthDate,
    String breed,
    String gender,
    boolean isNeutered,
    String active,
    double weight,
    String information,
    List<String> imageUrls,
    ShelterResponse shelter
) {

    private record ShelterResponse(
        Long shelterId,
        String name,
        String imageUrl,
        String email,
        String address
    ) {

    }

    public static FindAnimalDetail from(Animal animal) {
        return new FindAnimalDetail(
            animal.getName(),
            animal.getBirthDate(),
            animal.getBreed(),
            animal.getGender().getName(),
            animal.isNeutered(),
            animal.getActive().getName(),
            animal.getWeight(),
            animal.getInformation(),
            animal.getImageUrls(),
            new ShelterResponse(
                animal.getShelter().getShelterId(),
                animal.getShelter().getName(),
                animal.getShelter().getImageUrl(),
                animal.getShelter().getEmail(),
                animal.getShelter().getAddress()
            )
        );
    }

}
