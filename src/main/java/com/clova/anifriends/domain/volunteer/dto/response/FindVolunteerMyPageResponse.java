package com.clova.anifriends.domain.volunteer.dto.response;

import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.time.LocalDate;

public record FindVolunteerMyPageResponse(
    String email,
    String name,
    LocalDate birthDate,
    String phoneNumber,
    int temperature,
    long volunteerCount,
    String imageUrl
) {

    public static FindVolunteerMyPageResponse from(Volunteer volunteer) {
        return new FindVolunteerMyPageResponse(
            volunteer.getEmail(),
            volunteer.getName(),
            volunteer.getBirthDate(),
            volunteer.getPhoneNumber(),
            volunteer.getTemperature(),
            volunteer.getApplicants().stream()
                .filter(applicant -> applicant.getStatus().equals(ApplicantStatus.ATTENDANCE))
                .count(),
            volunteer.getVolunteerImageUrl()
        );
    }
}
