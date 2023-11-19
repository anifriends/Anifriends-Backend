package com.clova.anifriends.global.firebase.controller;

import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.global.firebase.dto.FCMTestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class FCMTestControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("FCM 토큰 테스트 API 호출 시")
    void pushNotification() throws Exception {
        // given
        FCMTestRequest request = new FCMTestRequest("token");

        // when
        ResultActions result = mockMvc.perform(post("/test/fcm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("deviceToken").type(STRING).description("fcm device token")
                )
            ));
    }
}
