package com.clova.anifriends.domain.volunteer.dto.response;

import com.clova.anifriends.domain.volunteer.Volunteer;

public record RegisterVolunteerResponse(Long volunteerId) {

    public static RegisterVolunteerResponse from(Volunteer volunteer) {
        return new RegisterVolunteerResponse(volunteer.getVolunteerId());
    }
}
