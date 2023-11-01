package com.clova.anifriends.domain.volunteer.dto.response;

import com.clova.anifriends.domain.volunteer.Volunteer;

public record FindVolunteerProfileResponse(
    String email,
    String name,
    int temperature,
    String phoneNumber,
    String imageUrl
) {

    public static FindVolunteerProfileResponse from(
        Volunteer volunteer
    ) {
        return new FindVolunteerProfileResponse(
            volunteer.getEmail(),
            volunteer.getName(),
            volunteer.getTemperature(),
            volunteer.getPhoneNumber(),
            volunteer.getVolunteerImageUrl()
        );
    }
}
