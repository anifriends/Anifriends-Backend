package com.clova.anifriends.global.security.authorize;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

class UnauthorizedEntryPointTest extends BaseControllerTest {

    @Nested
    @DisplayName("commence 메서드 호출 시")
    class CommenceTest {

        @Test
        @DisplayName("성공")
        void commence() throws Exception {
            //given
            //when
            ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/shelters/recruitments"));

            //then
            resultActions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.UN_AUTHORIZATION.getValue()))
                .andExpect(jsonPath("$.message").isString());
        }
    }
}