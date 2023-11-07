package com.clova.anifriends.domain.auth.jwt.response;

import com.clova.anifriends.domain.auth.jwt.UserRole;

public record TokenResponse(Long userId, UserRole role, String accessToken, String refreshToken) {

    public static TokenResponse of(
        Long userId,
        UserRole userRole,
        String accessToken,
        String refreshToken) {
        return new TokenResponse(userId, userRole, accessToken, refreshToken);
    }
}
