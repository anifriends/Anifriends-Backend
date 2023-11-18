package com.clova.anifriends.domain.animal.service;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.exception.AnimalNotFoundException;
import com.clova.anifriends.domain.animal.mapper.AnimalMapper;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.time.LocalDate;
import java.util.List;
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
    private final ImageRemover imageRemover;

    @Transactional
    public RegisterAnimalResponse registerAnimal(
        Long shelterId, RegisterAnimalRequest registerAnimalRequest) {
        Shelter shelter = getShelterById(shelterId);
        Animal animal = AnimalMapper.toAnimal(shelter, registerAnimalRequest);
        animalRepository.save(animal);
        return RegisterAnimalResponse.from(animal);
    }

    @Transactional(readOnly = true)
    public FindAnimalDetail findAnimalDetail(Long animalId) {
        return FindAnimalDetail.from(getAnimalByAnimalId(animalId));
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

    @Transactional(readOnly = true)
    public FindAnimalsResponse findAnimals(
        AnimalType type,
        AnimalActive active,
        Boolean isNeutered,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        Pageable pageable) {
        Page<Animal> animalsWithPagination = animalRepository.findAnimals(
            type,
            active,
            isNeutered,
            age,
            gender,
            size,
            pageable
        );

        return FindAnimalsResponse.from(animalsWithPagination);
    }

    @Transactional
    public void updateAnimalAdoptStatus(Long shelterId, Long animalId, Boolean isAdopted) {
        Animal animal = getAnimalByAnimalIdAndShelterId(animalId, shelterId);
        animal.updateAdoptStatus(isAdopted);
    }

    @Transactional
    public void updateAnimal(
        Long shelterId,
        Long animalId,
        String name,
        LocalDate birthDate,
        AnimalType type,
        String breed,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        Double weight,
        String information,
        List<String> imageUrls
    ) {
        Animal foundAnimal = getAnimalByAnimalIdAndShelterIdWithImages(animalId, shelterId);
        imageRemover.deleteImages(foundAnimal.findImagesToDelete(imageUrls));
        foundAnimal.updateAnimal(name, birthDate, type, breed, gender, isNeutered, active, weight,
            information, imageUrls);
    }

    @Transactional
    public void deleteAnimal(Long shelterId, Long animalId) {
        Animal animal = getAnimalByAnimalIdAndShelterId(animalId, shelterId);
        imageRemover.deleteImages(animal.getImages());
        animalRepository.delete(animal);
    }

    private Shelter getShelterById(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }

    private Animal getAnimalByAnimalId(Long animalId) {
        return animalRepository.findById(animalId)
            .orElseThrow(() -> new AnimalNotFoundException("존재하지 않는 보호 동물입니다."));
    }

    private Animal getAnimalByAnimalIdAndShelterId(Long animalId, Long shelterId) {
        return animalRepository.findByShelterIdAndAnimalId(shelterId, animalId)
            .orElseThrow(() -> new AnimalNotFoundException("존재하지 않는 보호 동물입니다."));
    }

    private Animal getAnimalByAnimalIdAndShelterIdWithImages(Long animalId, Long shelterId) {
        return animalRepository.findByAnimalIdAndShelterIdWithImages(animalId, shelterId)
            .orElseThrow(() -> new AnimalNotFoundException("존재하지 않는 보호 동물입니다."));
    }
}
