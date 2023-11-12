package com.clova.anifriends.domain.applicant.service.dto;

public record UpdateApplicantAttendanceCommand(
    Long applicantId,
    Boolean isAttended
) {

}
