package com.clova.anifriends.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.auth.controller.request.LoginRequest;
import com.clova.anifriends.domain.auth.service.response.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("봉사자 로그인 api 호출 시")
    class VolunteerLoginTest {

        @Test
        @DisplayName("성공")
        void volunteerLogin() throws Exception {
            //given
            LoginRequest request = new LoginRequest("email@email.com", "password123!");
            TokenResponse response = new TokenResponse("accessToken", "refreshToken");

            given(authService.volunteerLogin(any(), any())).willReturn(response);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/volunteers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("email").type(STRING).description("봉사자 이메일"),
                        fieldWithPath("password").type(STRING).description("봉사자 패스워드")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(STRING).description("액세스 토큰"),
                        fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰")
                    )
                ));
        }
    }
    @Nested
    @DisplayName("보호소 로그인 api 호출 시")
    class ShelterLoginTest {

        @Test
        @DisplayName("성공")
        void shelterLogin() throws Exception {
            //given
            LoginRequest request = new LoginRequest("email@email.com", "password123!");
            TokenResponse response = new TokenResponse("accessToken", "refreshToken");

            given(authService.shelterLogin(any(), any())).willReturn(response);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/shelters/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("email").type(STRING).description("보호소 이메일"),
                        fieldWithPath("password").type(STRING).description("보호소 패스워드")
                    ),
                    responseFields(
                        fieldWithPath("accessToken").type(STRING).description("액세스 토큰"),
                        fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰")
                    )
                ));
        }
    }
}
