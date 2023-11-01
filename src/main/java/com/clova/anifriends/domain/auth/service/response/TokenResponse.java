package com.clova.anifriends.domain.auth.service.response;

import com.clova.anifriends.domain.auth.jwt.response.UserToken;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse from(UserToken memberToken) {
        return new TokenResponse(memberToken.accessToken(), memberToken.refreshToken());
    }
}
