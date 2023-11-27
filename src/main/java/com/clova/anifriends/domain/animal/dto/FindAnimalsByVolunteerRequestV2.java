package com.clova.anifriends.domain.animal.dto;

import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import java.time.LocalDateTime;

public record FindAnimalsByVolunteerRequestV2(
    AnimalType type,
    AnimalGender gender,
    AnimalNeuteredFilter neuteredFilter,
    AnimalActive active,
    AnimalSize animalSize,
    AnimalAge age,
    Long animalId,
    LocalDateTime createdAt

) {

}
