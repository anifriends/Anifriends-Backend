package com.clova.anifriends.domain.applicant.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsApprovedResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ApplicantControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("봉사 신청 API 호출 시")
    void registerApplicant() throws Exception {
        // given
        doNothing().when(applicantService).registerApplicant(anyLong(), anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/volunteers/recruitments/{recruitmentId}/apply", 1L)
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(headerWithName("Authorization").description("액세스 토큰"))
            ));
    }

    @Test
    @DisplayName("봉사 신청 승인자 조회 API 호출 시")
    void findApplicantApproved() throws Exception {
        // given
        FindApplicantsApprovedResponse.from(List.of());

        when(applicantService.findApplicantsApproved(anyLong(), anyLong()))
            .thenReturn(FindApplicantsApprovedResponse.from(List.of()));

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/recruitments/{recruitmentId}/approval", 1L)
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                    requestHeaders(
                        headerWithName("Authorization").description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집글 ID")
                    ),
                    responseFields(
                        fieldWithPath("applicants[]").type(ARRAY).description("봉사 신청자 리스트").optional(),
                        fieldWithPath("applicants[].applicantId").type(NUMBER).description("봉사 신청 ID"),
                        fieldWithPath("applicants[].volunteerId").type(NUMBER).description("봉사자 ID"),
                        fieldWithPath("applicants[].birthdate").type(STRING).description("봉사자 생일"),
                        fieldWithPath("applicants[].gender").type(STRING).description("봉사 성별"),
                        fieldWithPath("applicants[].phoneNumber").type(STRING).description("봉사자 전화번호"),
                        fieldWithPath("applicants[].attendance").type(BOOLEAN).description("출석 현황")
                    )
                )
            );
    }
}
