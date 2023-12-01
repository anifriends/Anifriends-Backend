package com.clova.anifriends.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.auth.dto.request.LoginRequest;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 봉사자 로그인 api 호출 시")
    void volunteerLogin() throws Exception {
        //given
        LoginRequest request = new LoginRequest("email@email.com", "password123!", "token");
        TokenResponse response = new TokenResponse(1L, UserRole.ROLE_VOLUNTEER, "accessToken",
            "refreshToken");

        given(authService.volunteerLogin(any(), any(), any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/volunteers/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(STRING).description("봉사자 이메일"),
                    fieldWithPath("password").type(STRING).description("봉사자 패스워드"),
                    fieldWithPath("deviceToken").type(STRING).description("fcm device token")
                        .optional()
                ),
                responseCookies(
                    cookieWithName("volunteerRefreshToken").description("리프레시 토큰")
                ),
                responseFields(
                    fieldWithPath("userId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("role").type(STRING).description("사용자 역할"),
                    fieldWithPath("accessToken").type(STRING).description("액세스 토큰")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호소 로그인 api 호출 시")
    void shelterLogin() throws Exception {
        //given
        LoginRequest request = new LoginRequest("email@email.com", "password123!", "token");
        TokenResponse response = new TokenResponse(1L, UserRole.ROLE_SHELTER, "accessToken",
            "refreshToken");

        given(authService.shelterLogin(any(), any(), any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/shelters/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(STRING).description("보호소 이메일"),
                    fieldWithPath("password").type(STRING).description("보호소 패스워드"),
                    fieldWithPath("deviceToken").type(STRING).description("fcm device token")
                        .optional()
                ),
                responseCookies(
                    cookieWithName("shelterRefreshToken").description("리프레시 토큰")
                ),
                responseFields(
                    fieldWithPath("userId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("role").type(STRING).description("사용자 역할"),
                    fieldWithPath("accessToken").type(STRING).description("액세스 토큰")
                )
            ));
    }

    @Test
    @DisplayName("성공: 봉사자 액세스 토큰 갱신 api 호출 시")
    void volunteerRefreshAccessToken() throws Exception {
        //given
        TokenResponse tokenResponse = AuthFixture.userToken();
        Cookie refreshTokenCookie = AuthFixture.volunteerRefreshTokenCookie();

        given(authService.refreshAccessToken(anyString())).willReturn(tokenResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/volunteers/refresh")
            .cookie(refreshTokenCookie));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestCookies(
                    cookieWithName("volunteerRefreshToken").description("봉사자 리프레시 토큰")
                ),
                responseCookies(
                    cookieWithName("volunteerRefreshToken").description("갱신된 리프레시 토큰")
                ),
                responseFields(
                    fieldWithPath("userId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("role").type(STRING).description("사용자 역할"),
                    fieldWithPath("accessToken").type(STRING).description("갱신된 액세스 토큰")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호소 액세스 토큰 갱신 api 호출 시")
    void shelterRefreshAccessToken() throws Exception {
        //given
        TokenResponse tokenResponse = AuthFixture.userToken();
        Cookie refreshTokenCookie = AuthFixture.shelterRefreshTokenCookie();

        given(authService.refreshAccessToken(anyString())).willReturn(tokenResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/shelters/refresh")
            .cookie(refreshTokenCookie));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestCookies(
                    cookieWithName("shelterRefreshToken").description("보호소 리프레시 토큰")
                ),
                responseCookies(
                    cookieWithName("shelterRefreshToken").description("갱신된 보호소 리프레시 토큰")
                ),
                responseFields(
                    fieldWithPath("userId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("role").type(STRING).description("사용자 역할"),
                    fieldWithPath("accessToken").type(STRING).description("갱신된 액세스 토큰")
                )
            ));
    }

    @Test
    @DisplayName("예외(AuthAuthenticationException): 액세스 토큰 갱신 API 호출 시 쿠키가 포함되지 않은 경우")
    void exceptionWhenNullRefreshToken() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/shelters/refresh"));

        //then
        resultActions.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode")
                .value(ErrorCode.NOT_EXISTS_REFRESH_TOKEN.getValue()));
    }

    @Test
    @DisplayName("성공: 봉사자 로그아웃 API 호출 시")
    void volunteerLogout() throws Exception {
        //given
        Cookie refreshTokenCookie = AuthFixture.volunteerRefreshTokenCookie();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/volunteers/logout")
            .cookie(refreshTokenCookie));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestCookies(
                    cookieWithName("volunteerRefreshToken").description("봉사자 리프레시 토큰 쿠키")
                ),
                responseCookies(
                    cookieWithName("volunteerRefreshToken").description("리프레시 토큰 쿠키 무효화 설정 쿠키")
                )
            ));
    }

    @Test
    @DisplayName("예외(AuthAuthenticationException): 봉사자 로그아웃 API 호출 시 리프레시 토큰 쿠기가 포함되지 않은 경우")
    void exceptionWhenVolunteerLogoutWithoutRefreshTokenCookie() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/volunteers/logout")
            .header(AUTHORIZATION, volunteerAccessToken));

        //then
        resultActions.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode")
                .value(ErrorCode.NOT_EXISTS_REFRESH_TOKEN.getValue()));
    }

    @Test
    @DisplayName("성공: 봉사자 로그아웃 API 호출 시")
    void shelterLogout() throws Exception {
        //given
        Cookie refreshTokenCookie = AuthFixture.shelterRefreshTokenCookie();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/shelters/logout")
            .cookie(refreshTokenCookie));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestCookies(
                    cookieWithName("shelterRefreshToken").description("보호소 리프레시 토큰 쿠키")
                ),
                responseCookies(
                    cookieWithName("shelterRefreshToken").description("리프레시 토큰 쿠키 무효화 설정 쿠키")
                )
            ));
    }

    @Test
    @DisplayName("예외(AuthAuthenticationException): 보호소 로그아웃 API 호출 시 리프레시 토큰 쿠기가 포함되지 않은 경우")
    void exceptionWhenShelterLogoutWithoutRefreshTokenCookie() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/shelters/logout"));

        //then
        resultActions.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode")
                .value(ErrorCode.NOT_EXISTS_REFRESH_TOKEN.getValue()));
    }
}
