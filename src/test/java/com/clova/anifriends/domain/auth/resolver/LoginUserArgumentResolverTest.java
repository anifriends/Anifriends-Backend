package com.clova.anifriends.domain.auth.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.auth.exception.AuthAuthenticationException;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class LoginUserArgumentResolverTest extends BaseControllerTest {

    @Nested
    @DisplayName("@LoginUser를 사용하는 경우")
    class LoginUserTest {

        String accessToken = AuthFixture.volunteerAccessToken();

        @Test
        @DisplayName("성공: 액세스 토큰이 포함되어 있으면")
        void loginUser() throws Exception {
            //given

            //when
            ResultActions resultActions = mockMvc.perform(get("/test/login-user")
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andDo(print());
        }

        @Test
        @DisplayName("예외(AuthAuthenticationException): 액세스 토큰이 포함되어 있지 않으면")
        void exceptionWhenNotContainsAccessToken() throws Exception {
            //given
            //when
            //then
            mockMvc.perform(get("/test/login-user"))
                .andExpect(
                    result -> {
                        Exception resolvedException = result.getResolvedException();
                        assertThat(resolvedException).isInstanceOf(
                            AuthAuthenticationException.class);
                    }
                ).andDo(print());
        }

        @Test
        @DisplayName("무시: Long 이외의 파라미터와 사용한 경우")
        void ignoreWhenUsingWithInvalidParameter() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/test/login-user/invalid")
                .header(AUTHORIZATION, accessToken));

            //then
            resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        }
    }

    @Nested
    @DisplayName("@AuthenticationPrincipal을 사용하는 경우")
    class AuthenticationPrincipalTest {

        @Test
        @DisplayName("성공")
        void authenticationPrincipal() throws Exception {
            //given

            //when
            ResultActions resultActions = mockMvc.perform(get("/test/authentication-principal")
                .header(AUTHORIZATION, shelterAccessToken));

            //then
            resultActions.andExpect(status().isOk());
        }
    }
}
