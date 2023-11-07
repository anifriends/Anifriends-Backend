package com.clova.anifriends.domain.animal.dto.request;

import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;

public record FindAnimalsByVolunteerRequest(
    AnimalType type,
    AnimalGender gender,
    Boolean isNeutered,
    AnimalActive active,
    AnimalSize size,
    AnimalAge age
) {

}
