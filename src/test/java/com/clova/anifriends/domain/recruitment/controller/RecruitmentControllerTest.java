package com.clova.anifriends.domain.recruitment.controller;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.fixture.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentResponse;
import com.clova.anifriends.domain.shelter.Shelter;
import org.junit.jupiter.api.DisplayName;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RecruitmentControllerTest extends BaseControllerTest {

    @DisplayName("findRecruitmentById 실행 시")
    void FindRecruitmentTest() throws Exception {
        // given
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        FindRecruitmentResponse response = findRecruitmentResponse(recruitment);

        when(recruitmentService.findRecruitmentById(anyLong())).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/recruitments/{recruitmentId}", recruitment.getRecruitmentId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("title").type(STRING).description("제목"),
                    fieldWithPath("capacity").type(NUMBER).description("정원"),
                    fieldWithPath("applicantCount").type(NUMBER).description("봉사 신청 인원"),
                    fieldWithPath("content").type(STRING).description("내용"),
                    fieldWithPath("startTime").type(STRING).description("봉사 시작 시간"),
                    fieldWithPath("endTime").type(STRING).description("봉사 종료 시간"),
                    fieldWithPath("isClosed").type(BOOLEAN).description("마감 여부"),
                    fieldWithPath("deadline").type(STRING).description("마감 시간"),
                    fieldWithPath("deadline").type(STRING).description("마감 날짜와 시간"),
                    fieldWithPath("createdAt").type(STRING).description("게시글 생성 시간"),
                    fieldWithPath("updatedAt").type(STRING).description("게시글 수정 시간"),
                    fieldWithPath("imageUrls[]").type(STRING).description("이미지 url 리스트")
                )
            ));

    }

    private OngoingStubbing<FindRecruitmentResponse> when(FindRecruitmentResponse recruitmentById) {
        return null;
    }

}