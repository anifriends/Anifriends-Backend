package com.clova.anifriends.domain.applicant.dto;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
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
        LocalDate volunteerBirthDate,
        VolunteerGender volunteerGender,
        Integer completedVolunteerCount,
        Integer volunteerTemperature,
        String applicantStatus
    ) {

        public static FindApplicantResponse from(Applicant applicant) {
            return new FindApplicantResponse(
                applicant.getVolunteer().getVolunteerId(),
                applicant.getApplicantId(),
                applicant.getVolunteer().getBirthDate(),
                applicant.getVolunteer().getGender(),
                applicant.getVolunteer().getApplicantCompletedCount(),
                applicant.getVolunteer().getTemperature(),
                applicant.getStatus().convertToApprovalStatus().name()
            );
        }
    }

    public static FindApplicantsResponse from(List<Applicant> applicants, Recruitment recruitment) {
        return new FindApplicantsResponse(
            applicants.stream()
                .map(FindApplicantResponse::from)
                .toList(),
            recruitment.getCapacity()
        );
    }
}
