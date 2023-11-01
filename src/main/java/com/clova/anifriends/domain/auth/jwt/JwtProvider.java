package com.clova.anifriends.domain.auth.jwt;


import com.clova.anifriends.domain.auth.jwt.response.CustomClaims;
import com.clova.anifriends.domain.auth.jwt.response.UserToken;

public interface JwtProvider {

    UserToken createToken(Long userId, UserRole userRole);

    CustomClaims parseAccessToken(String token);

    UserToken refreshAccessToken(String refreshToken);
}
