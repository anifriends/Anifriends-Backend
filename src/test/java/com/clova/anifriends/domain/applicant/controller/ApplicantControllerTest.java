package com.clova.anifriends.domain.applicant.controller;

import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.PENDING;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
    @DisplayName("findApplyingVolunteers 실행 시")
    void findApplyingVolunteers() throws Exception {
        // given
        Long volunteerId = 1L;
        Long recruitmentId = 1L;
        Long applicantShouldWriteReviewId = 1L;
        Long applicantShouldNotWriteReviewId = 2L;

        Shelter shelter = ShelterFixture.shelter();
        Volunteer volunteer = VolunteerFixture.volunteer();
        setField(volunteer, "volunteerId", volunteerId);

        Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
        setField(recruitment, "recruitmentId", recruitmentId);

        Applicant applicantShouldWriteReview = ApplicantFixture.applicant(recruitment,
            volunteer, ATTENDANCE);
        Applicant applicantShouldNotWriteReview = ApplicantFixture.applicant(recruitment,
            volunteer, PENDING);

        setField(applicantShouldWriteReview, "applicantId", applicantShouldWriteReviewId);
        setField(applicantShouldNotWriteReview, "applicantId", applicantShouldNotWriteReviewId);

        Review review = ReviewFixture.review(applicantShouldWriteReview);
        setField(review, "reviewId", 1L);

        FindApplyingVolunteersResponse findApplyingVolunteersResponse = FindApplyingVolunteersResponse.from(
            List.of(
                applicantShouldWriteReview,
                applicantShouldNotWriteReview
            )
        );

        given(applicantService.findApplyingVolunteers(volunteerId)).willReturn(
            findApplyingVolunteersResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/applicants")
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("findApplyingVolunteerResponses").type(JsonFieldType.ARRAY)
                        .description("신청한 봉사 리스트"),
                    fieldWithPath("findApplyingVolunteerResponses[].recruitmentId").type(
                            JsonFieldType.NUMBER)
                        .description("봉사 모집글 ID"),
                    fieldWithPath("findApplyingVolunteerResponses[].applicantId").type(
                            JsonFieldType.NUMBER)
                        .description("봉사 신청자 ID"),
                    fieldWithPath("findApplyingVolunteerResponses[].title").type(
                            JsonFieldType.STRING)
                        .description("모집글 제목"),
                    fieldWithPath("findApplyingVolunteerResponses[].shelterName").type(
                            JsonFieldType.STRING)
                        .description("보호소 이름"),
                    fieldWithPath("findApplyingVolunteerResponses[].status").type(
                            JsonFieldType.STRING)
                        .description("승인 상태"),
                    fieldWithPath("findApplyingVolunteerResponses[].isWritedReview").type(
                            JsonFieldType.BOOLEAN)
                        .description("후기 작성 가능 여부"),
                    fieldWithPath("findApplyingVolunteerResponses[].volunteerDate").type(
                            JsonFieldType.STRING)
                        .description("봉사 날짜")
                )
            ));
    }
}
