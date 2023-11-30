package com.clova.anifriends.domain.applicant.dto;

import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.util.List;

public record FindApplicantsResponse(
    List<FindApplicantResponse> applicants,
    Integer recruitmentCapacity
) {

    public record FindApplicantResponse(
        Long volunteerId,
        Long applicantId,
        String volunteerName,
        LocalDate volunteerBirthDate,
        VolunteerGender volunteerGender,
        Integer completedVolunteerCount,
        Integer volunteerTemperature,
        String applicantStatus
    ) {

    }
}
