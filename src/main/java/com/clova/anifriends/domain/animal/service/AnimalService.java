package com.clova.anifriends.domain.animal.service;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByVolunteerResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.exception.AnimalNotFoundException;
import com.clova.anifriends.domain.animal.mapper.AnimalMapper;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public FindAnimalByVolunteerResponse findAnimalByIdByVolunteer(Long animalId) {
        return FindAnimalByVolunteerResponse.from(getAnimalById(animalId));
    }

    @Transactional(readOnly = true)
    public FindAnimalByShelterResponse findAnimalByShelter(Long animalId, Long shelterId) {
        return FindAnimalByShelterResponse.from(
            getAnimalByAnimalIdAndShelterIdWithImages(animalId, shelterId));
    }

    @Transactional(readOnly = true)
    public FindAnimalsByShelterResponse findAnimalsByShelter(
        Long shelterId,
        String keyword,
        AnimalType type,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        AnimalSize size,
        AnimalAge age,
        Pageable pageable
    ) {
        Page<Animal> animals = animalRepository.findAnimalsByShelter(
            shelterId,
            keyword,
            type,
            gender,
            isNeutered,
            active,
            size,
            age,
            pageable
        );

        return FindAnimalsByShelterResponse.from(animals);
    }


    public FindAnimalsByVolunteerResponse findAnimalsByVolunteer(
        AnimalType type,
        AnimalActive active,
        Boolean isNeutered,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        Pageable pageable
    ) {
        Page<Animal> animalsWithPagination = animalRepository.findAnimalsByVolunteer(
            type,
            active,
            isNeutered,
            age,
            gender,
            size,
            pageable
        );

        return FindAnimalsByVolunteerResponse.from(animalsWithPagination);
    }

    private Animal getAnimalById(Long animalId) {
        return animalRepository.findById(animalId)
            .orElseThrow(() -> new AnimalNotFoundException("존재하지 않는 보호 동물입니다."));
    }

    private Animal getAnimalByAnimalIdAndShelterIdWithImages(Long animalId, Long shelterId) {
        return animalRepository.findByAnimalIdAndShelterIdWithImages(animalId, shelterId)
            .orElseThrow(() -> new AnimalNotFoundException("존재하지 않는 보호 동물입니다."));

    }
}
