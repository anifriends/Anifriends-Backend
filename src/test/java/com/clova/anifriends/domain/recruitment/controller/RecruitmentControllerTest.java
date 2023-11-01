package com.clova.anifriends.domain.recruitment.controller;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentByVolunteerResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.fixture.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentByVolunteerResponse;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RecruitmentControllerTest extends BaseControllerTest {

    @Test
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

    @Test
    @DisplayName("findRecruitmentByIdByVolunteer 실행 시")
    void findRecruitmentByIdByVolunteerTest() throws Exception {
        // given
        Shelter shelter = shelter();
        ShelterImage shelterImage = ShelterImageFixture.shelterImage(shelter);
        setField(shelter, "shelterImage", shelterImage);
        Recruitment recruitment = recruitment(shelter);
        FindRecruitmentByVolunteerResponse response = findRecruitmentByVolunteerResponse(
            recruitment);

        when(recruitmentService.findRecruitmentByIdByVolunteer(anyLong()))
            .thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/volunteers/recruitments/{recruitmentId}", anyLong())
                .header(AUTHORIZATION, volunteerAccessToken)
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
                    fieldWithPath("imageUrls[]").type(ARRAY).description("이미지 url 리스트"),
                    fieldWithPath("shelterInfo.shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("shelterInfo.address").type(STRING).description("보호소 주소"),
                    fieldWithPath("shelterInfo.imageUrl").type(STRING).description("보호소 이미지 url")
                        .optional(),
                    fieldWithPath("shelterInfo.email").type(STRING).description("보호소 이메일")
                )
            ));
    }
}
