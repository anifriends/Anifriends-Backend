package com.clova.anifriends.domain.volunteer.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckDuplicateVolunteerEmailRequest(
    @NotBlank(message = "이메일은 필수값입니다.")
    String email) {

}
