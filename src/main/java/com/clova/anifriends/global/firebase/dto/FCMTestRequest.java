package com.clova.anifriends.global.firebase.dto;

import jakarta.validation.constraints.NotBlank;

public record FCMTestRequest(
    @NotBlank(message = "deviceToken은 필수 입력 항목입니다.") String deviceToken
) {
}
