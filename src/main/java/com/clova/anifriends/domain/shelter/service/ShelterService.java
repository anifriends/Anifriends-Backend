package com.clova.anifriends.domain.shelter.service;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.CheckDuplicateShelterResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterSimpleByVolunteerResponse;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.wrapper.ShelterEmail;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 16;

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
            encodePassword(password),
            address,
            addressDetail,
            name,
            phoneNumber,
            sparePhoneNumber,
            isOpenedAddress);
        shelterRepository.save(shelter);
        return shelter.getShelterId();
    }

    private String encodePassword(String password) {
        validatePasswordNotNull(password);
        validatePasswordLength(password);
        return passwordEncoder.encodePassword(password);
    }

    @Transactional
    public void updatePassword(Long shelterId, String rawOldPassword, String rawNewPassword) {
        Shelter shelter = getShelter(shelterId);
        Shelter updatedShelter = shelter.updatePassword(
            passwordEncoder,
            rawOldPassword,
            rawNewPassword);
        shelterRepository.save(updatedShelter);
    }

    private void validatePasswordNotNull(String password) {
        if (Objects.isNull(password)) {
            throw new ShelterBadRequestException("패스워드는 필수값입니다.");
        }
    }

    private void validatePasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ShelterBadRequestException(
                MessageFormat.format("패스워드는 {0}자 이상, {1}자 이하여야 합니다.",
                    MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
        }
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
}
