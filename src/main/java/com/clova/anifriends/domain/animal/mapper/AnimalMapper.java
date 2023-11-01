package com.clova.anifriends.domain.animal.mapper;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.shelter.Shelter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class AnimalMapper {

    public static Animal toAnimal(Shelter shelter, RegisterAnimalRequest registerAnimalRequest) {
        return new Animal(
            shelter,
            registerAnimalRequest.name(),
            registerAnimalRequest.birthDate(),
            registerAnimalRequest.type(),
            registerAnimalRequest.breed(),
            registerAnimalRequest.gender(),
            registerAnimalRequest.isNeutered(),
            registerAnimalRequest.active(),
            registerAnimalRequest.weight(),
            registerAnimalRequest.information());
    }
}
