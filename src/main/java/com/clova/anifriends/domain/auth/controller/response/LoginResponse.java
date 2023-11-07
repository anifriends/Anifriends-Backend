package com.clova.anifriends.domain.auth.controller.response;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.jwt.response.TokenResponse;

public record LoginResponse(Long userId, UserRole role, String accessToken) {

    public static LoginResponse from(TokenResponse tokenResponse) {
        return new LoginResponse(
            tokenResponse.userId(),
            tokenResponse.role(),
            tokenResponse.accessToken());
    }
}
