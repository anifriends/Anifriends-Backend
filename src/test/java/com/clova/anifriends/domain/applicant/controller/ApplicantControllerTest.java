package com.clova.anifriends.domain.applicant.controller;

import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.PENDING;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.request.UpdateApplicantStatusRequest;
import com.clova.anifriends.domain.applicant.dto.request.UpdateApplicantsAttendanceRequest;
import com.clova.anifriends.domain.applicant.dto.request.UpdateApplicantsAttendanceRequest.UpdateApplicantAttendanceRequest;
import com.clova.anifriends.domain.applicant.dto.response.FindApplicantsApprovedResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
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
import org.springframework.test.util.ReflectionTestUtils;
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
        Long shelterId = 1L;
        Long volunteerId = 1L;
        Long recruitmentId = 1L;
        Long applicantShouldWriteReviewId = 1L;
        Long applicantShouldNotWriteReviewId = 2L;

        Shelter shelter = ShelterFixture.shelter();
        setField(shelter, "shelterId", shelterId);
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
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                responseFields(
                    fieldWithPath("findApplyingVolunteerResponses").type(JsonFieldType.ARRAY)
                        .description("신청한 봉사 리스트"),
                    fieldWithPath("findApplyingVolunteerResponses[].shelterId").type(
                            JsonFieldType.NUMBER)
                        .description("보호소 ID"),
                    fieldWithPath("findApplyingVolunteerResponses[].recruitmentId").type(
                            JsonFieldType.NUMBER)
                        .description("봉사 모집글 ID"),
                    fieldWithPath("findApplyingVolunteerResponses[].applicantId").type(
                            JsonFieldType.NUMBER)
                        .description("봉사 신청자 ID"),
                    fieldWithPath("findApplyingVolunteerResponses[].recruitmentTitle").type(
                            JsonFieldType.STRING)
                        .description("모집글 제목"),
                    fieldWithPath("findApplyingVolunteerResponses[].shelterName").type(
                            JsonFieldType.STRING)
                        .description("보호소 이름"),
                    fieldWithPath("findApplyingVolunteerResponses[].applicantStatus").type(
                            JsonFieldType.STRING)
                        .description("승인 상태"),
                    fieldWithPath("findApplyingVolunteerResponses[].applicantIsWritedReview").type(
                            JsonFieldType.BOOLEAN)
                        .description("후기 작성 가능 여부"),
                    fieldWithPath("findApplyingVolunteerResponses[].recruitmentStartTime").type(
                            JsonFieldType.STRING)
                        .description("봉사 날짜")
                )
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
                        fieldWithPath("applicants[].volunteerBirthdate").type(STRING)
                            .description("봉사자 생일"),
                        fieldWithPath("applicants[].volunteerGender").type(STRING)
                            .description("봉사자 성별"),
                        fieldWithPath("applicants[].volunteerPhoneNumber").type(STRING)
                            .description("봉사자 전화번호"),
                        fieldWithPath("applicants[].volunteerAttendance").type(BOOLEAN)
                            .description("출석 현황")
                    )
                )
            );
    }

    @Test
    @DisplayName("봉사 신청자 리스트 조회 API 호출 시")
    void findApplicant() throws Exception {
        // given
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", 1L);
        Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
        ReflectionTestUtils.setField(recruitment, "recruitmentId", 1L);

        FindApplicantsResponse response = FindApplicantsResponse.from(List.of(), recruitment);
        when(applicantService.findApplicants(anyLong(), anyLong())).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/recruitments/{recruitmentId}/applicants",
                recruitment.getRecruitmentId())
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
                        fieldWithPath("recruitmentCapacity").type(NUMBER).description("모집 정원"),
                        fieldWithPath("applicants[]").type(ARRAY).description("봉사 신청자 리스트").optional(),
                        fieldWithPath("applicants[].volunteerId").type(NUMBER).description("봉사자 ID"),
                        fieldWithPath("applicants[].applicantId").type(NUMBER).description("봉사 신청 ID"),
                        fieldWithPath("applicants[].volunteerName").type(NUMBER).description("봉사자 이름"),
                        fieldWithPath("applicants[].volunteerBirthDate").type(STRING)
                            .description("봉사자 생일"),
                        fieldWithPath("applicants[].volunteerGender").type(STRING)
                            .description("봉사자 성별"),
                        fieldWithPath("applicants[].completedVolunteerCount").type(STRING)
                            .description("봉사 횟수"),
                        fieldWithPath("applicants[].volunteerTemperature").type(BOOLEAN)
                            .description("봉사자 온도"),
                        fieldWithPath("applicants[].applicantStatus").type(BOOLEAN)
                            .description("신청 상태")
                    )
                )
            );
    }


    @Test
    @DisplayName("봉사자 출석 상태 수정 API 호출 시")
    void updateApplicantAttendance() throws Exception {
        // given
        Shelter shelter = ShelterFixture.shelter();
        Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
        ReflectionTestUtils.setField(recruitment, "recruitmentId", 1L);
        Volunteer volunteer = VolunteerFixture.volunteer();
        Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer, ATTENDANCE, 1L);

        UpdateApplicantAttendanceRequest updateApplicantAttendanceRequest = new UpdateApplicantAttendanceRequest(
            applicant.getApplicantId(),
            true
        );

        UpdateApplicantsAttendanceRequest updateApplicantsAttendanceRequest = new UpdateApplicantsAttendanceRequest(
            List.of(updateApplicantAttendanceRequest));

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/shelters/recruitments/{recruitmentId}/approval",
                recruitment.getRecruitmentId())
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateApplicantsAttendanceRequest))
        );

        // then
        result.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName("Authorization").description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("recruitmentId").description("모집글 ID")
                ),
                requestFields(
                    fieldWithPath("applicants[]").type(ARRAY).description("봉사 승인자 출석 리스트"),
                    fieldWithPath("applicants[].applicantId").type(NUMBER).description("봉사 신청 ID"),
                    fieldWithPath("applicants[].isAttended").type(BOOLEAN).description("출석 상태")
                )
            ));

    }

    @Test
    @DisplayName("봉사자 신청 상태 수정 API 호출 시")
    void updateApplicantStatus() throws Exception {
        // given
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", 1L);
        Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
        ReflectionTestUtils.setField(recruitment, "recruitmentId", 1L);
        Volunteer volunteer = VolunteerFixture.volunteer();
        Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer, PENDING, 1L);
        UpdateApplicantStatusRequest updateApplicantStatusRequest = new UpdateApplicantStatusRequest(
            true
        );

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/shelters/recruitments/{recruitmentId}/applicants/{applicantId}",
                recruitment.getRecruitmentId(), applicant.getApplicantId())
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateApplicantStatusRequest))
        );

        // then
        result.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName("Authorization").description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("recruitmentId").description("모집글 ID"),
                    parameterWithName("applicantId").description("봉사 신청 ID")
                ),
                requestFields(
                    fieldWithPath("isApproved").type(BOOLEAN).description("승인 상태")
                )
            ));
    }
}
