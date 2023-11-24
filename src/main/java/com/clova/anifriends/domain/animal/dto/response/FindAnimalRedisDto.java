package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;
import java.time.LocalDateTime;

public record FindAnimalRedisDto(
    Long animalId,
    String animalName,
    String shelterName,
    String shelterAddress,
    String animalImageUrl,
    LocalDateTime createdAt
) {

    public static FindAnimalRedisDto from(Animal animal) {
        return new FindAnimalRedisDto(
            animal.getAnimalId(),
            animal.getName(),
            animal.getShelter().getName(),
            animal.getShelter().getAddress(),
            animal.getImages().get(0),
            animal.getCreatedAt()
        );
    }
}
