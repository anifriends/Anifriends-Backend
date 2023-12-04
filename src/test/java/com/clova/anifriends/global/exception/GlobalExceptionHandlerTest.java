package com.clova.anifriends.global.exception;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class GlobalExceptionHandlerTest extends BaseControllerTest {

    @Nested
    @DisplayName("BadRequestException 발생 시")
    class ThrowBadRequestExceptionTest {

        @Test
        @DisplayName("성공: 상태 코드 400 반환")
        void return400() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/test/bad-request"));

            //then
            resultActions.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("AuthenticationException 발생 시")
    class ThrowAuthenticationExceptionTest {

        @Test
        @DisplayName("성공: 상태 코드 401 반환")
        void return401() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/test/authentication"));

            //then
            resultActions.andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("AuthorizationException 발생 시")
    class ThrowAuthorizationExceptionTest {

        @Test
        @DisplayName("성공: 상태 코드 403 반환")
        void return403() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/test/authorization"));

            //then
            resultActions.andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("NotFoundException 발생 시")
    class ThrowNotFoundExceptionTest {

        @Test
        @DisplayName("성공: 상태 코드 404 반환")
        void return404() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/test/not-found"));

            //then
            resultActions.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("ConflictException 발생 시")
    class ThrowConflictExceptionTest {

        @Test
        @DisplayName("성공: 상태 코드 409 반환")
        void return409() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(get("/test/conflict"));

            //then
            resultActions.andExpect(status().isConflict());
        }
    }

    @Test
    @DisplayName("성공: 인증되지 않은 사용자가 권한이 필요한 api 접근 시 401")
    void authenticationCredentialsNotFoundEx() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/test/access-denied"));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("성공: 권한 없는 사용자가 다른 권한의 api 접근 시 403")
    void accessDeniedExTest() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/test/access-denied"));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }
}
