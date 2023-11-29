package com.clova.anifriends.domain.animal.support.fixture;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.repository.response.FindAnimalsResult;

public class AnimalDtoFixture {

    public static FindAnimalDetail findAnimalDetail(Animal animal) {
        return FindAnimalDetail.from(animal);
    }

    public static RegisterAnimalRequest registerAnimal(Animal animal) {
        return new RegisterAnimalRequest(
            animal.getName(),
            animal.getBirthDate(),
            animal.getType().name(),
            animal.getBreed(),
            animal.getGender().name(),
            animal.isNeutered(),
            animal.getActive().name(),
            animal.getWeight(),
            animal.getInformation(),
            animal.getImages()
        );
    }

    public static FindAnimalsResult findAnimalsResult(Animal animal) {
        return new FindAnimalsResult(
            animal.getAnimalId(),
            animal.getName(),
            animal.getCreatedAt(),
            animal.getShelter().getName(),
            animal.getShelter().getAddress(),
            animal.getImages().get(0));
    }

}
