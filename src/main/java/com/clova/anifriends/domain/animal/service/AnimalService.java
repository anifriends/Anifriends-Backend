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
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AnimalCacheService animalCacheService;

    @Transactional
    public RegisterAnimalResponse registerAnimal(
        Long shelterId, RegisterAnimalRequest registerAnimalRequest) {
        Shelter shelter = getShelterById(shelterId);
        Animal animal = AnimalMapper.toAnimal(shelter, registerAnimalRequest);
        animalRepository.save(animal);
        animalCacheService.increaseTotalNumberOfAnimals();
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
        AnimalNeuteredFilter neuteredFilter,
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
            neuteredFilter,
            active,
            size,
            age,
            pageable
        );

        return FindAnimalsByShelterResponse.from(animals);
    }

    @Transactional(readOnly = true)
    public FindAnimalsResponse findAnimalsByVolunteer(
        AnimalType type,
        AnimalActive active,
        AnimalNeuteredFilter neuteredFilter,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        Pageable pageable) {
        Page<Animal> animalsWithPagination = animalRepository.findAnimalsByVolunteer(
            type,
            active,
            neuteredFilter,
            age,
            gender,
            size,
            pageable
        );

        return FindAnimalsResponse.from(animalsWithPagination);
    }

    @Transactional(readOnly = true)
    public FindAnimalsResponse findAnimalsByVolunteerV2(
        AnimalType type,
        AnimalActive active,
        AnimalNeuteredFilter neuteredFilter,
        AnimalAge age,
        AnimalGender gender,
        AnimalSize size,
        LocalDateTime createdAt,
        Long animalId,
        Pageable pageable
    ) {
        Slice<Animal> animalsWithPagination = animalRepository.findAnimalsByVolunteerV2(
            type,
            active,
            neuteredFilter,
            age,
            gender,
            size,
            createdAt,
            animalId,
            pageable
        );
        if (isFirstPage(type, active, neuteredFilter, age, gender, size, createdAt, animalId)) {
            return FindAnimalsResponse.fromV2(animalsWithPagination,
                animalCacheService.getTotalNumberOfAnimals());
        }
        Long count = animalRepository.countAnimalsV2(
            type,
            active,
            neuteredFilter,
            age,
            gender,
            size
        );

        return FindAnimalsResponse.fromV2(animalsWithPagination, count);
    }

    @Transactional
    public void updateAnimalAdoptStatus(Long shelterId, Long animalId, Boolean isAdopted) {
        Animal animal = getAnimalByAnimalIdAndShelterId(animalId, shelterId);
        animal.updateAdoptStatus(isAdopted);
        if (isAdopted) {
            animalCacheService.decreaseTotalNumberOfAnimals();
        }
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

        List<String> imagesToDelete = foundAnimal.findImagesToDelete(imageUrls);
        applicationEventPublisher.publishEvent(new ImageDeletionEvent(imagesToDelete));

        foundAnimal.updateAnimal(name, birthDate, type, breed, gender, isNeutered, active, weight,
            information, imageUrls);
    }

    @Transactional
    public void deleteAnimal(Long shelterId, Long animalId) {
        Animal animal = getAnimalByAnimalIdAndShelterId(animalId, shelterId);
        List<String> imagesToDelete = animal.getImages();
        applicationEventPublisher.publishEvent(new ImageDeletionEvent(imagesToDelete));
        animalRepository.delete(animal);
        animalCacheService.decreaseTotalNumberOfAnimals();
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

    private boolean isFirstPage(AnimalType type, AnimalActive active,
        AnimalNeuteredFilter neuteredFilter, AnimalAge age, AnimalGender gender, AnimalSize size,
        LocalDateTime createdAt, Long animalId) {
        return type == null && active == null && neuteredFilter == null && age == null
            && gender == null && size == null && createdAt == null && animalId == null;
    }
}
