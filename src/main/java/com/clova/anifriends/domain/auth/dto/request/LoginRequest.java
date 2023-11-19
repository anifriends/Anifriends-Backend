package com.clova.anifriends.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    String email,
    @NotBlank(message = "패스워드는 공백일 수 없습니다.")
    String password,
    String deviceToken
) {

}
