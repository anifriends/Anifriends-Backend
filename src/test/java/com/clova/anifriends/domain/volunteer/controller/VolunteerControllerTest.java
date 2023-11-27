package com.clova.anifriends.domain.volunteer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.dto.request.CheckDuplicateVolunteerEmailRequest;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.request.UpdateVolunteerInfoRequest;
import com.clova.anifriends.domain.volunteer.dto.request.UpdateVolunteerPasswordRequest;
import com.clova.anifriends.domain.volunteer.dto.response.CheckDuplicateVolunteerEmailResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerProfileResponse;
import com.clova.anifriends.domain.volunteer.dto.response.RegisterVolunteerResponse;
import com.clova.anifriends.domain.volunteer.support.VolunteerDtoFixture;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class VolunteerControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("봉사자 이메일 중복 확인 API 호출 시")
    void checkDuplicateVolunteerEmail() throws Exception {
        //given
        CheckDuplicateVolunteerEmailRequest checkDuplicateVolunteerEmailRequest
            = VolunteerDtoFixture.checkDuplicateVolunteerEmailRequest();
        CheckDuplicateVolunteerEmailResponse checkDuplicateVolunteerEmailResponse
            = new CheckDuplicateVolunteerEmailResponse(false);

        given(volunteerService.checkDuplicateVolunteerEmail(anyString()))
            .willReturn(checkDuplicateVolunteerEmailResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/volunteers/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(checkDuplicateVolunteerEmailRequest)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(STRING).description("사용자 이메일")
                        .attributes(DocumentationFormatGenerator.getConstraint("@를 포함한 이메일 형식"))
                ),
                responseFields(
                    fieldWithPath("isDuplicated").type(BOOLEAN).description("이메일 중복 여부")
                )
            ));
    }

    @Test
    @DisplayName("봉사자 회원가입 API 호출 시")
    void registerVolunteer() throws Exception {
        // given
        RegisterVolunteerRequest registerVolunteerRequest = VolunteerDtoFixture.registerVolunteerRequest();
        RegisterVolunteerResponse registerVolunteerResponse = new RegisterVolunteerResponse(1L);
        given(volunteerService.registerVolunteer(any(), any(), any(), any(), any(),
            any())).willReturn(registerVolunteerResponse);

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
                ),
                responseFields(
                    fieldWithPath("volunteerId").type(NUMBER).description("생성된 봉사자 ID")
                )
            ));
    }

    @Test
    @DisplayName("봉사자 마이페이지 조회 API 호출 시")
    void findVolunteerMyPage() throws Exception {
        // given
        Volunteer volunteer = VolunteerFixture.volunteer();
        ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
        FindVolunteerMyPageResponse findVolunteerMyPageResponse = VolunteerDtoFixture.findVolunteerMyPageResponse(
            volunteer);

        given(volunteerService.findVolunteerMyPage(anyLong())).willReturn(
            findVolunteerMyPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/me")
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(headerWithName("Authorization").description("액세스 토큰")),
                responseFields(
                    fieldWithPath("volunteerId").type(NUMBER).description("봉사자 ID"),
                    fieldWithPath("volunteerEmail").type(STRING).description("이메일"),
                    fieldWithPath("volunteerName").type(STRING).description("이름"),
                    fieldWithPath("volunteerBirthDate").type(STRING).description("생년월일"),
                    fieldWithPath("volunteerPhoneNumber").type(STRING).description("전화번호"),
                    fieldWithPath("volunteerTemperature").type(NUMBER).description("온도"),
                    fieldWithPath("completedVolunteerCount").type(NUMBER).description("봉사 횟수"),
                    fieldWithPath("volunteerImageUrl").type(STRING).description("프로필 이미지 URL")
                        .optional(),
                    fieldWithPath("volunteerGender").type(STRING).description("성별")
                )
            ));
    }

    @Test
    @DisplayName("봉사자 프로필 조회 API 호출 시")
    void findVolunteerProfile() throws Exception {
        // given
        Long volunteerId = 1L;
        Volunteer volunteer = VolunteerFixture.volunteer();

        FindVolunteerProfileResponse findVolunteerMyPageResponse = FindVolunteerProfileResponse.from(
            volunteer
        );

        given(volunteerService.findVolunteerProfile(anyLong())).willReturn(
            findVolunteerMyPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/shelters/volunteers/{volunteerId}/profile", volunteerId)
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("volunteerId").description("봉사자 ID")
                ),
                responseFields(
                    fieldWithPath("volunteerEmail").type(STRING).description("이메일"),
                    fieldWithPath("volunteerName").type(STRING).description("이름"),
                    fieldWithPath("volunteerTemperature").type(NUMBER).description("온도"),
                    fieldWithPath("volunteerPhoneNumber").type(STRING).description("전화번호"),
                    fieldWithPath("volunteerImageUrl").type(STRING).description("프로필 이미지 URL")
                        .optional()
                )
            ));
    }

    @Test
    @DisplayName("봉사자 계정 정보 업데이트 api 호출 시")
    void updateVolunteerInfo() throws Exception {
        //given
        UpdateVolunteerInfoRequest updateVolunteerInfoRequest
            = new UpdateVolunteerInfoRequest("새로운이름", VolunteerGender.MALE, LocalDate.now(),
            "010-9999-9999", "www.aws.s3.com/2");

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/volunteers/me")
            .header(AUTHORIZATION, volunteerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateVolunteerInfoRequest)));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("name").type(STRING).description("변경할 봉사자 이름")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 10자 이하")),
                    fieldWithPath("gender").type(STRING).description("변경할 봉사자 성별")
                        .attributes(DocumentationFormatGenerator.getConstraint("MALE, FEMALE")),
                    fieldWithPath("birthDate").type(STRING).description("변경할 봉사자 생년월일")
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    fieldWithPath("phoneNumber").type(STRING).description("변경할 봉사자 전화번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("-을 포함한 전화번호 형식")),
                    fieldWithPath("imageUrl").type(STRING).description("변경할 봉사자 이미지 url")
                )
            ));
    }

    @Test
    @DisplayName("봉사자 비밀번호 변경 api 호출 시")
    void updatePassword() throws Exception {
        //given
        UpdateVolunteerPasswordRequest updateVolunteerPasswordRequest = new UpdateVolunteerPasswordRequest(
            "oldPassword123!", "newPassword123!");

        //when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/volunteers/me/passwords")
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateVolunteerPasswordRequest)));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("oldPassword").type(STRING).description("현재 비밀번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("6자 이상, 16자 이하")),
                    fieldWithPath("newPassword").type(STRING).description("변경할 비밀번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("6자 이상, 16자 이하"))
                )
            ));
    }
}
