package com.clova.anifriends.domain.shelter.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateShelterPasswordRequest(
    @NotBlank(message = "현재 비밀번호는 필수값입니다.")
    String oldPassword,
    @NotBlank(message = "변경할 비밀번호는 필수값입니다.")
    String newPassword) {

}
