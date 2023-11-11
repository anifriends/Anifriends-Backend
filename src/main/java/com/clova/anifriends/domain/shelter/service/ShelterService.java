package com.clova.anifriends.domain.shelter.service;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.response.CheckDuplicateShelterResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterSimpleByVolunteerResponse;
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
    private final CustomPasswordEncoder passwordEncoder;

    @Transactional
    public Long registerShelter(
        String email,
        String password,
        String name,
        String address,
        String addressDetail,
        String phoneNumber,
        String sparePhoneNumber,
        boolean isOpenedAddress) {
        Shelter shelter = new Shelter(
            email,
            password,
            address,
            addressDetail,
            name,
            phoneNumber,
            sparePhoneNumber,
            isOpenedAddress,
            passwordEncoder);
        shelterRepository.save(shelter);
        return shelter.getShelterId();
    }

    @Transactional
    public void updatePassword(Long shelterId, String rawOldPassword, String rawNewPassword) {
        Shelter shelter = getShelter(shelterId);
        shelter.updatePassword(passwordEncoder, rawOldPassword, rawNewPassword);
    }

    @Transactional(readOnly = true)
    public CheckDuplicateShelterResponse checkDuplicateEmail(String email) {
        boolean isDuplicated = shelterRepository.existsByEmail(new ShelterEmail(email));
        return CheckDuplicateShelterResponse.from(isDuplicated);
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

    @Transactional(readOnly = true)
    public FindShelterSimpleByVolunteerResponse findShelterSimpleByVolunteer(
        Long shelterId
    ) {
        Shelter foundShelter = getShelter(shelterId);

        return FindShelterSimpleByVolunteerResponse.from(foundShelter);
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
    }

    @Transactional
    public void updateAddressStatus(
        Long shelterId,
        Boolean isOpenedAddress
    ) {
        Shelter foundShelter = getShelter(shelterId);
        foundShelter.updateAddressStatus(isOpenedAddress);
    }
}
