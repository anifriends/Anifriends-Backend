package com.clova.anifriends.domain.volunteer.service;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.dto.response.CheckDuplicateVolunteerEmailResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerProfileResponse;
import com.clova.anifriends.domain.volunteer.dto.response.RegisterVolunteerResponse;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.vo.VolunteerEmail;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public CheckDuplicateVolunteerEmailResponse checkDuplicateVolunteerEmail(String email) {
        boolean isDuplicated = volunteerRepository.existsByEmail(new VolunteerEmail(email));
        return CheckDuplicateVolunteerEmailResponse.from(isDuplicated);
    }

    @Transactional
    public RegisterVolunteerResponse registerVolunteer(
        String email,
        String password,
        String name,
        String birthDate,
        String phoneNumber,
        String gender
    ) {
        Volunteer volunteer = new Volunteer(email, password, birthDate, phoneNumber, gender, name,
            passwordEncoder);
        volunteerRepository.save(volunteer);
        return RegisterVolunteerResponse.from(volunteer);
    }

    @Transactional(readOnly = true)
    public FindVolunteerMyPageResponse findVolunteerMyPage(Long volunteerId) {
        return FindVolunteerMyPageResponse.from(getVolunteer(volunteerId));
    }

    @Transactional(readOnly = true)
    public FindVolunteerProfileResponse findVolunteerProfile(
        Long volunteerId
    ) {
        Volunteer foundVolunteer = getVolunteer(volunteerId);

        return FindVolunteerProfileResponse.from(
            foundVolunteer
        );
    }

    @Transactional
    public void updateVolunteerInfo(
        Long volunteerId,
        String name,
        VolunteerGender gender,
        LocalDate birthDate,
        String phoneNumber,
        String imageUrl
    ) {
        Volunteer volunteer = getVolunteer(volunteerId);
        deleteImageFromS3(volunteer, imageUrl);
        volunteer.updateVolunteerInfo(name, gender, birthDate, phoneNumber, imageUrl);
    }

    @Transactional
    public void updatePassword(
        Long volunteerId,
        String rawOldPassword,
        String rawNewPassword
    ) {
        Volunteer foundVolunteer = getVolunteer(volunteerId);
        foundVolunteer.updatePassword(passwordEncoder, rawOldPassword, rawNewPassword);
    }

    private void deleteImageFromS3(Volunteer volunteer, String newImageUrl) {
        volunteer.findImageToDelete(newImageUrl)
            .ifPresent(imageUrl -> applicationEventPublisher
                .publishEvent(new ImageDeletionEvent(List.of(imageUrl))));
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }
}
