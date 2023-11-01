package com.clova.anifriends.domain.auth.jwt.response;

import java.util.List;

public record CustomClaims(Long memberId, List<String> authorities) {

    public static CustomClaims of(Long memberId, List<String> authorities) {
        return new CustomClaims(memberId, authorities);
    }
}
