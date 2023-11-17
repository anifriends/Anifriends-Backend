package com.clova.anifriends.domain.animal.support.fixture;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;

public class AnimalDtoFixture {

    public static FindAnimalDetail findAnimalDetail(Animal animal) {
        return FindAnimalDetail.from(animal);
    }

}
