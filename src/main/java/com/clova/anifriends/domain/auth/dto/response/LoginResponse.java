package com.clova.anifriends.domain.auth.dto.response;

import com.clova.anifriends.domain.auth.jwt.UserRole;

public record LoginResponse(Long userId, UserRole role, String accessToken) {

    public static LoginResponse from(TokenResponse tokenResponse) {
        return new LoginResponse(
            tokenResponse.userId(),
            tokenResponse.role(),
            tokenResponse.accessToken());
    }
}
