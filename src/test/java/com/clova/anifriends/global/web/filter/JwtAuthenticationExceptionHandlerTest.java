package com.clova.anifriends.global.web.filter;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.security.jwt.JJwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class JwtAuthenticationExceptionHandlerTest extends BaseControllerTest {

    @Nested
    @DisplayName("JWT 필터 예외 발생 시")
    class DoFilterInternalTest {

        @Test
        @DisplayName("성공: 올바른 형식의 토큰")
        void doFilterInternal() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/api/shelters/recruitments")
                .header(AUTHORIZATION, shelterAccessToken));

            //then
            resultActions.andExpect(status().isOk());
        }

        @Test
        @DisplayName("성공(ExpiredAccessTokenException): 만료된 토큰 예외 핸들링")
        void handleExpiredAccessTokenEx() throws Exception {
            //given
            JJwtProvider jJwtProvider = AuthFixture.jJwtProvider();
            ReflectionTestUtils.setField(jJwtProvider, "expirySeconds", -1000);
            TokenResponse token = jJwtProvider.createToken(1L, UserRole.ROLE_SHELTER);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/shelters/recruitments")
                .header(AUTHORIZATION, "Bearer " + token.accessToken()));

            //then
            resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.ACCESS_TOKEN_EXPIRED.getValue()))
                .andExpect(jsonPath("$.message").isString());
        }

        @ParameterizedTest
        @CsvSource({"asdf", "Bearer ", "Bearer asdf"})
        @DisplayName("성공(InvalidJwtException): 올바르지 않은 토큰 예외 핸들링")
        void handleInvalidJwtEx(String invalidToken) throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/api/shelters/recruitments")
                .header(AUTHORIZATION, invalidToken));

            //then
            resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.UN_AUTHENTICATION.getValue()))
                .andExpect(jsonPath("$.message").isString());
        }
    }
}