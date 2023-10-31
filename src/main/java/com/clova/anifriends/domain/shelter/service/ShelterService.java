package com.clova.anifriends.domain.shelter.service;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterImageNotFoundException;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterImageRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final ShelterImageRepository shelterImageRepository;

    @Transactional(readOnly = true)
    public FindShelterDetailResponse findShelterDetail(
        Long shelterId
    ) {
        Shelter foundShelter = shelterRepository.findById(shelterId)
            .orElseThrow(
                () -> new ShelterNotFoundException(ErrorCode.NOT_FOUND, "존재하지 않는 보호소입니다."));

        ShelterImage foundShelterImage = shelterImageRepository.findShelterImageByShelter(
                foundShelter)
            .orElseThrow(() -> new ShelterImageNotFoundException(ErrorCode.NOT_FOUND,
                "존재하지 않는 보호소 이미지입니다."));

        return FindShelterDetailResponse.of(
            foundShelter,
            foundShelterImage
        );
    }
}
