package com.clova.anifriends.domain.volunteer.service;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
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

}
