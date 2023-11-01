package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import java.time.LocalDate;
import java.util.List;

public record FindAnimalByVolunteerResponse(
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

    public static FindAnimalByVolunteerResponse from(Animal animal) {
        return new FindAnimalByVolunteerResponse(
            animal.getName(),
            animal.getBirthDate(),
            animal.getBreed(),
            animal.getGender().getValue(),
            animal.isNeutered(),
            animal.getActive().getValue(),
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
