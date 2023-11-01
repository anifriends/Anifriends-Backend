package com.clova.anifriends.domain.recruitment.controller;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.fixture.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class RecruitmentControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 봉사 모집글 등록 api 호출")
    void registerRecruitment() throws Exception {
        //given
        String title = "title";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);
        LocalDateTime deadline = now.plusHours(5);
        int capacity = 10;
        String content = "content";
        List<String> imageUrls = new ArrayList<>();
        RegisterRecruitmentRequest request = new RegisterRecruitmentRequest(
            title, startTime, endTime, deadline, capacity, content, imageUrls);
        RegisterRecruitmentResponse response = new RegisterRecruitmentResponse(1L);

        given(recruitmentService.registerRecruitment(anyLong(), any())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/shelters/recruitments")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("봉사 모집글 제목")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 50자 이하")),
                    fieldWithPath("startTime").type(JsonFieldType.STRING).description("봉사 시작 시간")
                        .attributes(DocumentationFormatGenerator.getDatetimeConstraint()),
                    fieldWithPath("endTime").type(JsonFieldType.STRING).description("봉사 종료 시간")
                        .attributes(DocumentationFormatGenerator.getDatetimeConstraint()),
                    fieldWithPath("deadline").type(JsonFieldType.STRING).description("봉사 모집 마감 시간")
                        .attributes(DocumentationFormatGenerator.getDatetimeConstraint()),
                    fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("봉사 모집 정원")
                        .description(DocumentationFormatGenerator.getConstraint("1명 이상, 99명 이하")),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("봉사 모집글 본문")
                        .description(DocumentationFormatGenerator.getConstraint("1자 이상, 1000자 이하")),
                    fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("봉사 모집글 이미지")
                        .description(DocumentationFormatGenerator.getConstraint("0장 이상, 5장 이하"))
                        .optional()
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스에 대한 접근 api")
                )
            ));
    }

    @DisplayName("findRecruitmentById 실행 시")
    void FindRecruitmentTest() throws Exception {
        // given
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        FindRecruitmentByShelterResponse response = findRecruitmentResponse(recruitment);

        when(recruitmentService.findRecruitmentByIdByShelter(anyLong()))
            .thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/recruitments/{recruitmentId}", anyLong())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("recruitmentId").description("봉사 모집글 ID")
                ),
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
                    fieldWithPath("createdAt").type(STRING).description("게시글 생성 시간").optional(),
                    fieldWithPath("updatedAt").type(STRING).description("게시글 수정 시간").optional(),
                    fieldWithPath("imageUrls[]").type(ARRAY).description("이미지 url 리스트")
                )
            ));

    }
}
