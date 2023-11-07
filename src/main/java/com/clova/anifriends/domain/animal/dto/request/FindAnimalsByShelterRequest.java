package com.clova.anifriends.domain.animal.dto.request;

import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;

public record FindAnimalsByShelterRequest(
    String keyword,
    AnimalType type,
    AnimalGender gender,
    Boolean isNeutered,
    AnimalActive active,
    AnimalSize size,
    AnimalAge age
) {

    public FindAnimalsByShelterRequest(
        String keyword,
        AnimalType type,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        AnimalSize size,
        AnimalAge age
    ) {
        this.keyword = keyword;
        this.type = type;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.active = active;
        this.size = size;
        this.age = age;
    }
}
