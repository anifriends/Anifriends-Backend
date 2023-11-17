package com.clova.anifriends.domain.auth.support;

import com.clova.anifriends.global.security.jwt.JJwtProvider;
import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;

public final class AuthFixture {

    private static final String ISSUER = "issuer";
    private static final int EXPIRY_SECONDS = 1000;
    private static final int REFRESH_EXPIRY_SECONDS = 10000;
    private static final String TEST_SECRET = "}:ASV~lS,%!I:ba^GBR<Q@cJN~!,Y0=zx7Rqwum+remZ>ayhI3$4dX$jx~@9[1F";
    private static final String TEST_REFRESH_SECRET = "~GWW.|?:\"#Rqmm^-nk#>#4Ngc}]3xz!hOQCXNF:8z-Mdn\"U!Vt</+/8;ATR*lc{";
    public static final Long USER_ID = 1L;
    public static final UserRole USER_ROLE = UserRole.ROLE_VOLUNTEER;
    private static final String BEARER = "Bearer ";

    public static JwtProvider jwtProvider() {
        return jJwtProvider();
    }

    public static JJwtProvider jJwtProvider() {
        return new JJwtProvider(ISSUER, EXPIRY_SECONDS, REFRESH_EXPIRY_SECONDS, TEST_SECRET,
            TEST_REFRESH_SECRET);
    }

    public static String shelterAccessToken() {
        TokenResponse tokenResponse = jwtProvider().createToken(USER_ID, UserRole.ROLE_SHELTER);
        return BEARER + tokenResponse.accessToken();
    }

    public static String volunteerAccessToken() {
        TokenResponse tokenResponse = jwtProvider().createToken(USER_ID, USER_ROLE);
        return BEARER + tokenResponse.accessToken();
    }

    public static TokenResponse userToken() {
        return jwtProvider().createToken(USER_ID, USER_ROLE);
    }
}
