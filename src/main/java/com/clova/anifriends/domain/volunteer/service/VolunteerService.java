package com.clova.anifriends.domain.volunteer.service;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.response.GetVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerImageRepository;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerImageRepository volunteerImageRepository;

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
    public GetVolunteerMyPageResponse getVolunteerMyPage(Long volunteerId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        String imageUrl = getVolunteerImageUrl(volunteer);
        return GetVolunteerMyPageResponse.of(volunteer, imageUrl);
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }

    private String getVolunteerImageUrl(Volunteer volunteer) {
        return volunteerImageRepository.findByVolunteer(volunteer)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자 이미지입니다."))
            .getImageUrl();
    }
}
