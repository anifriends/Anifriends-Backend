package com.clova.anifriends.domain.shelter.service;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;

    @Transactional(readOnly = true)
    public FindShelterDetailResponse findShelterDetail(
        Long shelterId
    ) {
        Shelter foundShelter = shelterRepository.findById(shelterId)
            .orElseThrow(
                () -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));

        return FindShelterDetailResponse.from(
            foundShelter
        );
    }
}
