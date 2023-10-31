package com.clova.anifriends.domain.volunteer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.support.VolunteerDtoFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class VolunteerControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("봉사자 회원가입 API 호출 시")
    void registerVolunteer() throws Exception {
        // given
        RegisterVolunteerRequest registerVolunteerRequest = VolunteerDtoFixture.registerVolunteerRequest();
        given(volunteerService.registerVolunteer(any())).willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerVolunteerRequest)));

        // then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(STRING).description("이메일"),
                    fieldWithPath("password").type(STRING).description("비밀번호"),
                    fieldWithPath("name").type(STRING).description("이름"),
                    fieldWithPath("birthDate").type(STRING).description("생년월일"),
                    fieldWithPath("phoneNumber").type(STRING).description("전화번호"),
                    fieldWithPath("gender").type(STRING).description("성별")
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스 위치")
                )
            ));
    }
}
