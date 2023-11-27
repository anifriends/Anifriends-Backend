package com.clova.anifriends.domain.animal.support.fixture;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;

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

}
