package com.clova.anifriends.domain.volunteer.dto.response;

import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;

public record FindVolunteerMyPageResponse(
    long volunteerId,
    String volunteerEmail,
    String volunteerName,
    LocalDate volunteerBirthDate,
    String volunteerPhoneNumber,
    int volunteerTemperature,
    long completedVolunteerCount,
    String volunteerImageUrl,
    VolunteerGender volunteerGender
) {

    public static FindVolunteerMyPageResponse from(Volunteer volunteer) {
        return new FindVolunteerMyPageResponse(
            volunteer.getVolunteerId(),
            volunteer.getEmail(),
            volunteer.getName(),
            volunteer.getBirthDate(),
            volunteer.getPhoneNumber(),
            volunteer.getTemperature(),
            volunteer.getApplicants().stream()
                .filter(applicant -> applicant.getStatus().equals(ApplicantStatus.ATTENDANCE))
                .count(),
            volunteer.getVolunteerImageUrl(),
            volunteer.getGender()
        );
    }
}
