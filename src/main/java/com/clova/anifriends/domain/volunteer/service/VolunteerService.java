package com.clova.anifriends.domain.volunteer.service;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerProfileResponse;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Transactional
    public Long registerVolunteer(RegisterVolunteerRequest registerVolunteerRequest) {
        Volunteer volunteer = new Volunteer(
            registerVolunteerRequest.email(),
            registerVolunteerRequest.password(),
            registerVolunteerRequest.birthDate(),
            registerVolunteerRequest.phoneNumber(),
            registerVolunteerRequest.gender(),
            registerVolunteerRequest.name()
        );

        volunteerRepository.save(volunteer);
        return volunteer.getVolunteerId();
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

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }
}
