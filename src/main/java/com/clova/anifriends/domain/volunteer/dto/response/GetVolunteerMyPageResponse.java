package com.clova.anifriends.domain.volunteer.dto.response;

import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.Volunteer;

public record GetVolunteerMyPageResponse(
    String email,
    String name,
    String birthDate,
    String phoneNumber,
    int temperature,
    int volunteerCount,
    String imageUrl
) {

    public static GetVolunteerMyPageResponse from(Volunteer volunteer) {
        return new GetVolunteerMyPageResponse(
            volunteer.getEmail(),
            volunteer.getName(),
            volunteer.getBirthDate(),
            volunteer.getPhoneNumber(),
            volunteer.getTemperature(),
            volunteer.getApplications().stream().filter(applicant -> applicant.getStatus().equals(
                ApplicantStatus.ATTENDANCE.getValue())).count(),
            volunteer.getImageUrl()
        );
    }
}
