package com.clova.anifriends.domain.shelter.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckDuplicateShelterEmailRequest(
    @NotBlank(message = "이메일은 필수값입니다.")
    String email) {

}
