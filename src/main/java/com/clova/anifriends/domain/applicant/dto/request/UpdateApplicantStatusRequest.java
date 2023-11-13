package com.clova.anifriends.domain.applicant.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateApplicantStatusRequest(
    @NotNull(message = "isApproved는 필수 입력 항목입니다.") Boolean isApproved
) {

}
