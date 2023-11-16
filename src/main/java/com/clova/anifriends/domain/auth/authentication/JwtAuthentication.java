package com.clova.anifriends.domain.auth.authentication;

import com.clova.anifriends.domain.auth.jwt.UserRole;

public record JwtAuthentication(Long userId, UserRole role, String accessToken) {

}
