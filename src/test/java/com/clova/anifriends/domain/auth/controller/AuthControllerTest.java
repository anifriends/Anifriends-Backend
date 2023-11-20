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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.auth.dto.request.LoginRequest;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
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
                    cookieWithName("refreshToken").description("리프레시 토큰")
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
                    cookieWithName("refreshToken").description("리프레시 토큰")
                ),
                responseFields(
                    fieldWithPath("userId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("role").type(STRING).description("사용자 역할"),
                    fieldWithPath("accessToken").type(STRING).description("액세스 토큰")
                )
            ));
    }

    @Test
    @DisplayName("성공: 액세스 토큰 갱신 api 호출 시")
    void refreshAccessToken() throws Exception {
        //given
        TokenResponse tokenResponse = AuthFixture.userToken();
        MockCookie refreshToken = new MockCookie("refreshToken", tokenResponse.refreshToken());
        refreshToken.setPath("/api/auth");
        refreshToken.setHttpOnly(true);
        refreshToken.setDomain("localhost");
        refreshToken.setSameSite("None");

        given(authService.refreshAccessToken(anyString())).willReturn(tokenResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/auth/refresh")
            .cookie(refreshToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestCookies(
                    cookieWithName("refreshToken").description("리프레시 토큰")
                ),
                responseCookies(
                    cookieWithName("refreshToken").description("갱신된 리프레시 토큰")
                ),
                responseFields(
                    fieldWithPath("userId").type(NUMBER).description("사용자 ID"),
                    fieldWithPath("role").type(STRING).description("사용자 역할"),
                    fieldWithPath("accessToken").type(STRING).description("갱신된 액세스 토큰")
                )
            ));
    }
}
