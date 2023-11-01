package com.clova.anifriends.domain.animal.dto.response;

import com.clova.anifriends.domain.animal.Animal;

public record RegisterAnimalResponse(Long animalId) {

    public static RegisterAnimalResponse from(Animal animal) {
        return new RegisterAnimalResponse(animal.getAnimalId());
    }
}
