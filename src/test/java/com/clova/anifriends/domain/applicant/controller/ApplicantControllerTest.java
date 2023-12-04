package com.clova.anifriends.domain.applicant.controller;

import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.PENDING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse.FindApplicantResponse;
import com.clova.anifriends.domain.applicant.dto.request.UpdateApplicantStatusRequest;
import com.clova.anifriends.domain.applicant.dto.request.UpdateApplicantsAttendanceRequest;
import com.clova.anifriends.domain.applicant.dto.request.UpdateApplicantsAttendanceRequest.UpdateApplicantAttendanceRequest;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse.FindApplyingVolunteerResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApprovedApplicantsResponse;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.response.IsAppliedRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                requestHeaders(
                    headerWithName("Authorization").description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("recruitmentId").description("봉사 모집글 ID")
                )
            ));
    }

    @Test
    @DisplayName("봉사 신청 목록 조회 API 실행 시")
    void findApplyingVolunteers() throws Exception {
        // given
        FindApplyingVolunteerResponse response = new FindApplyingVolunteerResponse(
            1L,
            1L,
            1L,
            "모집글 제목",
            "보호소 이름",
            ATTENDANCE,
            true,
            LocalDateTime.now());
        PageInfo pageInfo = PageInfo.of(20, true);
        FindApplyingVolunteersResponse findApplyingVolunteersResponse
            = new FindApplyingVolunteersResponse(pageInfo, List.of(response));

        given(applicantService.findApplyingVolunteers(anyLong(), any()))
            .willReturn(findApplyingVolunteersResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/applicants")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("applicants")
                        .type(JsonFieldType.ARRAY).description("신청한 봉사 리스트"),
                    fieldWithPath("applicants[].shelterId")
                        .type(JsonFieldType.NUMBER).description("보호소 ID"),
                    fieldWithPath("applicants[].recruitmentId")
                        .type(JsonFieldType.NUMBER).description("봉사 모집글 ID"),
                    fieldWithPath("applicants[].applicantId")
                        .type(JsonFieldType.NUMBER).description("봉사 신청자 ID"),
                    fieldWithPath("applicants[].recruitmentTitle")
                        .type(JsonFieldType.STRING).description("모집글 제목"),
                    fieldWithPath("applicants[].shelterName")
                        .type(JsonFieldType.STRING).description("보호소 이름"),
                    fieldWithPath("applicants[].applicantStatus")
                        .type(JsonFieldType.STRING).description("승인 상태"),
                    fieldWithPath("applicants[].applicantIsWritedReview")
                        .type(JsonFieldType.BOOLEAN).description("후기 작성 가능 여부"),
                    fieldWithPath("applicants[].recruitmentStartTime")
                        .type(JsonFieldType.STRING).description("봉사 날짜")
                )
            ));
    }

    @Test
    @DisplayName("봉사 신청 승인자 조회 API 호출 시")
    void findApprovedApplicants() throws Exception {
        // given
        FindApprovedApplicantsResponse.from(List.of());

        when(applicantService.findApprovedApplicants(anyLong(), anyLong()))
            .thenReturn(FindApprovedApplicantsResponse.from(List.of()));

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
    void findApplicants() throws Exception {
        // given
        FindApplicantResponse findApplicantResponse = new FindApplicantResponse(
            1L,
            1L,
            "봉사자 이름",
            LocalDate.now(),
            VolunteerGender.MALE,
            5,
            36,
            ATTENDANCE.convertToApprovalStatus().getName()
        );
        FindApplicantsResponse findApplicantsResponse = new FindApplicantsResponse(
            List.of(findApplicantResponse), 10);

        given(applicantService.findApplicants(anyLong(), anyLong()))
            .willReturn(findApplicantsResponse);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/recruitments/{recruitmentId}/applicants", 1L)
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
                        fieldWithPath("applicants[].volunteerName").type(STRING).description("봉사자 이름"),
                        fieldWithPath("applicants[].volunteerBirthDate").type(STRING)
                            .description("봉사자 생일"),
                        fieldWithPath("applicants[].volunteerGender").type(STRING)
                            .description("봉사자 성별"),
                        fieldWithPath("applicants[].completedVolunteerCount").type(NUMBER)
                            .description("봉사 횟수"),
                        fieldWithPath("applicants[].volunteerTemperature").type(NUMBER)
                            .description("봉사자 온도"),
                        fieldWithPath("applicants[].applicantStatus").type(STRING)
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

    @Test
    @DisplayName("봉사 신청 여부 조회 API 호출 시")
    void isAppliedRecruitment() throws Exception {
        //given
        IsAppliedRecruitmentResponse isAppliedRecruitmentResponse
            = new IsAppliedRecruitmentResponse(true);

        given(applicantService.isAppliedRecruitment(anyLong(), anyLong()))
            .willReturn(isAppliedRecruitmentResponse);

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/recruitments/{recruitmentId}/apply", 1L)
                .header(AUTHORIZATION, volunteerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("recruitmentId").description("봉사 모집글 ID")
                ),
                responseFields(
                    fieldWithPath("isAppliedRecruitment").type(BOOLEAN).description("봉사 신청 여부")
                )
            ));
    }
}
