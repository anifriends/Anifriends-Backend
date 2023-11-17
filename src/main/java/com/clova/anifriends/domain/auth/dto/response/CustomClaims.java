package com.clova.anifriends.domain.auth.dto.response;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import java.util.List;

public record CustomClaims(Long memberId, UserRole role, List<String> authorities) {

    public static CustomClaims of(Long memberId, UserRole role, List<String> authorities) {
        return new CustomClaims(memberId, role, authorities);
    }
}
