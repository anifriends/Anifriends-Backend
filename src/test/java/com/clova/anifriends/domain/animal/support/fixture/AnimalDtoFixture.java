package com.clova.anifriends.domain.animal.support.fixture;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;

public class AnimalDtoFixture {

    public static FindAnimalByVolunteerResponse findAnimalByVolunteerResponse(Animal animal) {
        return FindAnimalByVolunteerResponse.from(animal);
    }

}
