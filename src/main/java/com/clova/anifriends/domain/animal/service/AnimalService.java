package com.clova.anifriends.domain.animal.service;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.mapper.AnimalMapper;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    @Transactional
    public RegisterAnimalResponse registerAnimal(
        Long shelterId, RegisterAnimalRequest registerAnimalRequest) {
        Shelter shelter = getShelterById(shelterId);
        Animal animal = AnimalMapper.toAnimal(shelter, registerAnimalRequest);
        animalRepository.save(animal);
        return RegisterAnimalResponse.from(animal);
    }

    private Shelter getShelterById(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }
}
