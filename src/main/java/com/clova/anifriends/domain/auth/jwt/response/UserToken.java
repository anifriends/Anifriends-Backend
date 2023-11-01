package com.clova.anifriends.domain.auth.jwt.response;

public record UserToken(String accessToken, String refreshToken) {

    public static UserToken of(String accessToken, String refreshToken) {
        return new UserToken(accessToken, refreshToken);
    }
}
