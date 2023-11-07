package com.clova.anifriends.domain.auth.jwt;


import com.clova.anifriends.domain.auth.jwt.response.CustomClaims;
import com.clova.anifriends.domain.auth.jwt.response.TokenResponse;

public interface JwtProvider {

    TokenResponse createToken(Long userId, UserRole userRole);

    CustomClaims parseAccessToken(String token);

    TokenResponse refreshAccessToken(String refreshToken);
}
