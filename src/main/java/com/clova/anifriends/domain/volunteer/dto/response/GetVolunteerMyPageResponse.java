package com.clova.anifriends.domain.volunteer.dto.response;

import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.time.LocalDate;

public record GetVolunteerMyPageResponse(
    String email,
    String name,
    LocalDate birthDate,
    String phoneNumber,
    int temperature,
    long volunteerCount,
    String imageUrl
) {

    public static GetVolunteerMyPageResponse of(Volunteer volunteer, String imageUrl){
        return new GetVolunteerMyPageResponse(
            volunteer.getEmail(),
            volunteer.getName(),
            volunteer.getBirthDate(),
            volunteer.getPhoneNumber(),
            volunteer.getTemperature(),
            volunteer.getApplications().stream().filter(applicant -> applicant.getStatus().equals(
                ApplicantStatus.ATTENDANCE.getValue())).count(),
            imageUrl
        );
    }
}
