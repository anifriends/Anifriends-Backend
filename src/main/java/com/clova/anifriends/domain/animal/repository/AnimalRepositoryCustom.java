package com.clova.anifriends.domain.animal.repository;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnimalRepositoryCustom {

    Page<Animal> findAnimalsByShelter(
        Long shelterId,
        String keyword,
        AnimalType type,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        AnimalSize size,
        AnimalAge age,
        Pageable pageable
    );

    Page<Animal> findAnimals(
        AnimalType type,
        AnimalActive active,
        Boolean isNeutered,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        Pageable pageable
    );
}
