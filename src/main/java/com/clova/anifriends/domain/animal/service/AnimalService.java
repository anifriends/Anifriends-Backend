package com.clova.anifriends.domain.animal.service;

import static com.clova.anifriends.global.exception.ErrorCode.NOT_FOUND;

import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.exception.NotFoundAnimalException;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    @Transactional(readOnly = true)
    public FindAnimalByVolunteerResponse findAnimalByIdByVolunteer(Long animalId) {
        return FindAnimalByVolunteerResponse.from(getAnimalById(animalId));
    }

    private Animal getAnimalById(Long animalId) {
        return animalRepository.findById(animalId)
            .orElseThrow(() -> new NotFoundAnimalException(NOT_FOUND, "존재하지 않는 보호 동물입니다."));
    }
}
