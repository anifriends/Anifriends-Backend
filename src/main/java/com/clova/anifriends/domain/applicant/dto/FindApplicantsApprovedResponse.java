package com.clova.anifriends.domain.applicant.dto;

import com.clova.anifriends.domain.applicant.Applicant;
import java.time.LocalDate;
import java.util.List;

public record FindApplicantsApprovedResponse(
    List<FindApplicant> applicants
) {

    private record FindApplicant(
        Long volunteerId,
        Long applicantId,
        String name,
        LocalDate birthDate,
        String gender,
        String phoneNumber,
        boolean attendance
    ) {

        private static FindApplicant from(Applicant applicant) {
            return new FindApplicant(
                applicant.getVolunteer().getVolunteerId(),
                applicant.getApplicantId(),
                applicant.getVolunteer().getName(),
                applicant.getVolunteer().getBirthDate(),
                applicant.getVolunteer().getGender(),
                applicant.getVolunteer().getPhoneNumber(),
                applicant.isAttendance()
            );
        }
    }

    public static FindApplicantsApprovedResponse from(List<Applicant> applicants) {
        return new FindApplicantsApprovedResponse(
            applicants.stream()
                .map(FindApplicant::from)
                .toList()
        );
    }

}
