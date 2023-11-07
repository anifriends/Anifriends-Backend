package com.clova.anifriends.domain.shelter.service;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.CheckDuplicateEmailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.wrapper.ShelterEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;

    @Transactional(readOnly = true)
    public CheckDuplicateEmailResponse checkDuplicateEmail(String email) {
        boolean isDuplicated = shelterRepository.existsByEmail(new ShelterEmail(email));
        return CheckDuplicateEmailResponse.from(isDuplicated);
    }

    @Transactional(readOnly = true)
    public FindShelterDetailResponse findShelterDetail(
        Long shelterId
    ) {
        Shelter foundShelter = getShelter(shelterId);

        return FindShelterDetailResponse.from(foundShelter);
    }

    @Transactional(readOnly = true)
    public FindShelterMyPageResponse findShelterMyPage(
        Long shelterId
    ) {
        Shelter foundShelter = getShelter(shelterId);

        return FindShelterMyPageResponse.from(foundShelter);
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }
}
