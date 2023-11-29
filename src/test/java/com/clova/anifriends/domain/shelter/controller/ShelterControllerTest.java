package com.clova.anifriends.domain.shelter.controller;

import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.request.CheckDuplicateShelterEmailRequest;
import com.clova.anifriends.domain.shelter.dto.request.RegisterShelterRequest;
import com.clova.anifriends.domain.shelter.dto.request.UpdateAddressStatusRequest;
import com.clova.anifriends.domain.shelter.dto.request.UpdateShelterPasswordRequest;
import com.clova.anifriends.domain.shelter.dto.request.UpdateShelterRequest;
import com.clova.anifriends.domain.shelter.dto.response.CheckDuplicateShelterResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.dto.response.FindShelterSimpleResponse;
import com.clova.anifriends.domain.shelter.dto.response.RegisterShelterResponse;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ShelterControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("보호소 이메일 중복 확인 api 호출 시")
    void checkDuplicateShelterEmail() throws Exception {
        //given
        CheckDuplicateShelterEmailRequest checkDuplicateShelterEmailRequest
            = new CheckDuplicateShelterEmailRequest("email@email.com");
        CheckDuplicateShelterResponse checkDuplicateShelterResponse
            = new CheckDuplicateShelterResponse(true);

        given(shelterService.checkDuplicateEmail(anyString())).willReturn(
            checkDuplicateShelterResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/shelters/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(checkDuplicateShelterEmailRequest)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("보호소 이메일")
                ),
                responseFields(
                    fieldWithPath("isDuplicated").type(JsonFieldType.BOOLEAN)
                        .description("이메일 중복 여부")
                )
            ));
    }

    @Test
    @DisplayName("보호소 회원가입 api 호출 시")
    void registerShelter() throws Exception {
        //given
        String email = "email@email.com";
        String password = "password123!";
        String name = "보호소 이름";
        String address = "보호소 주소";
        String addressDetail = "보호소 상세 주소";
        String phoneNumber = "보호소 전화번호";
        String sparePhoneNumber = "보호소 임시 전화번호";
        boolean isOpenedAddress = false;
        RegisterShelterRequest registerShelterRequest = new RegisterShelterRequest(email, password,
            name, address, addressDetail, phoneNumber, sparePhoneNumber, isOpenedAddress);
        RegisterShelterResponse registerShelterResponse = new RegisterShelterResponse(1L);

        given(shelterService.registerShelter(anyString(), anyString(), anyString(), anyString(),
            anyString(), anyString(), anyString(), anyBoolean()))
            .willReturn(registerShelterResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/shelters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerShelterRequest)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("보호소 이메일")
                        .attributes(DocumentationFormatGenerator.getConstraint("@ 포함")),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("보호소 패스워드")
                        .attributes(DocumentationFormatGenerator.getConstraint("6자 이상, 16자 이하")),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("보호소 이름")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                    fieldWithPath("address").type(JsonFieldType.STRING).description("보호소 주소")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 100자 이하")),
                    fieldWithPath("addressDetail").type(JsonFieldType.STRING)
                        .description("보호소 상세 주소")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 100자 이하")),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("보호소 전화번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("- 포함, 전화번호 형식 준수")),
                    fieldWithPath("sparePhoneNumber").type(JsonFieldType.STRING)
                        .description("보호소 임시 전화번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("- 포함, 전화번호 형식 준수")),
                    fieldWithPath("isOpenedAddress").type(JsonFieldType.BOOLEAN)
                        .description("보호소 주소 공개 여부")
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스 접근 가능 위치")
                ),
                responseFields(
                    fieldWithPath("shelterId").type(JsonFieldType.NUMBER).description("생성된 보호소 ID")
                )
            ));

    }

    @Test
    @DisplayName("findShelterDetail 실행 시")
    void findShelterDetail() throws Exception {
        // given
        Long shelterId = 1L;
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);
        FindShelterDetailResponse findShelterDetailResponse = new FindShelterDetailResponse(
            shelter.getShelterId(),
            shelter.getEmail(),
            shelter.getName(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getImage()
        );

        given(shelterService.findShelterDetail(shelterId)).willReturn(findShelterDetailResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/shelters/{shelterId}/profile", shelterId)
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("shelterId").description("보호소 ID")
                ),
                responseFields(
                    fieldWithPath("shelterId").type(JsonFieldType.NUMBER)
                        .description("보호소 ID"),
                    fieldWithPath("email").type(
                            JsonFieldType.STRING)
                        .description("보호소 이메일"),
                    fieldWithPath("name").type(
                            JsonFieldType.STRING)
                        .description("보호소 이름"),
                    fieldWithPath("address").type(
                            JsonFieldType.STRING)
                        .description("보호소 주소"),
                    fieldWithPath("addressDetail").type(
                            JsonFieldType.STRING)
                        .description("보호소 상세주소"),
                    fieldWithPath("phoneNumber").type(
                            JsonFieldType.STRING)
                        .description("보호소 전화번호"),
                    fieldWithPath("sparePhoneNumber").type(
                            JsonFieldType.STRING)
                        .description("보호소 임시 전화번호"),
                    fieldWithPath("imageUrl").type(
                            JsonFieldType.STRING)
                        .description("보호소 이미지 Url").optional()
                )
            ));
    }

    @Test
    @DisplayName("findShelterMyPage 실행 시")
    void findShelterMyPage() throws Exception {
        // given
        Long shelterId = 1L;
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);

        FindShelterMyPageResponse findShelterMyPageResponse = new FindShelterMyPageResponse(
            shelter.getShelterId(),
            shelter.getName(),
            shelter.getEmail(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.isOpenedAddress(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getImage()
        );

        given(shelterService.findShelterMyPage(shelterId)).willReturn(
            findShelterMyPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/shelters/me")
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("shelterId").type(JsonFieldType.NUMBER)
                        .description("보호소 ID"),
                    fieldWithPath("shelterName").type(JsonFieldType.STRING)
                        .description("보호소 이름"),
                    fieldWithPath("shelterEmail").type(JsonFieldType.STRING)
                        .description("보호소 이메일"),
                    fieldWithPath("shelterAddress").type(JsonFieldType.STRING)
                        .description("보호소 주소"),
                    fieldWithPath("shelterAddressDetail").type(JsonFieldType.STRING)
                        .description("보호소 상세주소"),
                    fieldWithPath("shelterIsOpenedAddress").type(JsonFieldType.BOOLEAN)
                        .description("보호소 상세 주소 공개 여부"),
                    fieldWithPath("shelterPhoneNumber").type(JsonFieldType.STRING)
                        .description("보호소 전화번호"),
                    fieldWithPath("shelterSparePhoneNumber").type(JsonFieldType.STRING)
                        .description("보호소 임시 전화번호"),
                    fieldWithPath("shelterImageUrl").type(JsonFieldType.STRING).optional()
                        .description("보호소 이미지 Url")
                )
            ));
    }

    @Test
    @DisplayName("findShelterSimple 실행 시")
    void findShelterSimple() throws Exception {
        // given
        Long shelterId = 1L;
        Shelter shelter = shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);
        FindShelterSimpleResponse response = FindShelterSimpleResponse.from(
            shelter);

        given(shelterService.findShelterSimple(shelterId)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/{shelterId}/profile/simple", shelterId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("shelterId").description("보호소 ID")
                ),
                responseFields(
                    fieldWithPath("shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("shelterEmail").type(STRING).description("보호소 이메일"),
                    fieldWithPath("shelterAddress").type(STRING).description("보호소 주소"),
                    fieldWithPath("shelterImageUrl").type(STRING).description("보호소 이미지 url")
                        .optional()
                )
            ));
    }

    @Test
    @DisplayName("보호소 비밀번호 변경 api 호출 시")
    void updatePassword() throws Exception {
        //given
        UpdateShelterPasswordRequest updateShelterPasswordRequest = new UpdateShelterPasswordRequest(
            "oldPassword123!", "newPassword123!");

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/shelters/me/passwords")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateShelterPasswordRequest)));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("oldPassword").type(STRING).description("현재 비밀번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("6자 이상, 16자 이하")),
                    fieldWithPath("newPassword").type(STRING).description("변경할 비밀번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("6자 이상, 16자 이하"))
                )
            ));
    }

    @Test
    @DisplayName("보호소 상세 주소 상태 수정 api 호출 시")
    void updateAddressStatus() throws Exception {
        //given
        UpdateAddressStatusRequest updateAddressStatusRequest = new UpdateAddressStatusRequest(
            false);

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/shelters/me/address/status")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateAddressStatusRequest)));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("isOpenedAddress").type(BOOLEAN).description("보호소 상세 주소 공개 여부")

                )
            ));
    }

    @Test
    @DisplayName("보호소 정보 수정 api 호출 시")
    void updateShelter() throws Exception {
        // given
        Shelter shelter = ShelterFixture.shelter();
        UpdateShelterRequest request = new UpdateShelterRequest(
            shelter.getName(), shelter.getImage(), shelter.getAddress(), shelter.getAddressDetail(),
            shelter.getPhoneNumber(), shelter.getSparePhoneNumber(), shelter.isOpenedAddress()
        );

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/shelters/me")
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("보호소 이름")
                            .attributes(
                                DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("보호소 이름")
                            .optional(),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("보호소 주소")
                            .attributes(
                                DocumentationFormatGenerator.getConstraint("1자 이상, 100자 이하")),
                        fieldWithPath("addressDetail").type(JsonFieldType.STRING)
                            .description("보호소 상세 주소")
                            .attributes(
                                DocumentationFormatGenerator.getConstraint("1자 이상, 100자 이하")),
                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                            .description("보호소 전화번호")
                            .attributes(
                                DocumentationFormatGenerator.getConstraint("- 포함, 전화번호 형식 준수")),
                        fieldWithPath("sparePhoneNumber").type(JsonFieldType.STRING)
                            .description("보호소 임시 전화번호")
                            .attributes(
                                DocumentationFormatGenerator.getConstraint("- 포함, 전화번호 형식 준수")),
                        fieldWithPath("isOpenedAddress").type(JsonFieldType.BOOLEAN)
                            .description("보호소 주소 공개 여부")
                    )
                ));


    }
}
