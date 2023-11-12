package com.clova.anifriends.domain.applicant.dto.request;

import java.util.List;

public record UpdateApplicantsAttendanceRequest(
    List<UpdateApplicantAttendanceRequest> applicants
) {

    public record UpdateApplicantAttendanceRequest(
        Long applicantId,
        Boolean isAttended
    ) {

    }
}
