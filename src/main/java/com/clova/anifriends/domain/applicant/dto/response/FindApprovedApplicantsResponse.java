package com.clova.anifriends.domain.applicant.dto.response;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.util.List;

public record FindApprovedApplicantsResponse(
    List<FindApprovedApplicantResponse> applicants
) {

    public record FindApprovedApplicantResponse(
        Long volunteerId,
        Long applicantId,
        String volunteerName,
        LocalDate volunteerBirthDate,
        VolunteerGender volunteerGender,
        String volunteerPhoneNumber,
        boolean volunteerAttendance
    ) {

        public static FindApprovedApplicantResponse from(Applicant applicant) {
            return new FindApprovedApplicantResponse(
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
}
