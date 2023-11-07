package com.clova.anifriends.domain.shelter.controller;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.dto.FindShelterMyPageResponse;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ShelterControllerTest extends BaseControllerTest {

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

        given(shelterService.registerShelter(anyString(), anyString(), anyString(), anyString(),
            anyString(), anyString(), anyString(), anyBoolean()))
            .willReturn(1L);

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
                    fieldWithPath("addressDetail").type(JsonFieldType.STRING).description("보호소 상세 주소")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 100자 이하")),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("보호소 전화번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("- 포함, 전화번호 형식 준수")),
                    fieldWithPath("sparePhoneNumber").type(JsonFieldType.STRING).description("보호소 임시 전화번호")
                        .attributes(DocumentationFormatGenerator.getConstraint("- 포함, 전화번호 형식 준수")),
                    fieldWithPath("isOpenedAddress").type(JsonFieldType.BOOLEAN).description("보호소 주소 공개 여부")
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스 접근 가능 위치")
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
            shelter.getShelterImageUrl()
        );

        given(shelterService.findShelterDetail(shelterId)).willReturn(findShelterDetailResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/shelters/{shelterId}/profile", shelterId)
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
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
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.isOpenedAddress(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getShelterImageUrl()
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
                    fieldWithPath("name").type(
                            JsonFieldType.STRING)
                        .description("보호소 이름"),
                    fieldWithPath("address").type(
                            JsonFieldType.STRING)
                        .description("보호소 주소"),
                    fieldWithPath("addressDetail").type(
                            JsonFieldType.STRING)
                        .description("보호소 상세주소"),
                    fieldWithPath("isOpenedAddress").type(
                            JsonFieldType.BOOLEAN)
                        .description("상세 주소 공개 여부"),
                    fieldWithPath("phoneNumber").type(
                            JsonFieldType.STRING)
                        .description("보호소 전화번호"),
                    fieldWithPath("sparePhoneNumber").type(
                            JsonFieldType.STRING)
                        .description("보호소 임시 전화번호"),
                    fieldWithPath("imageUrl").type(
                            JsonFieldType.STRING).optional()
                        .description("보호소 이미지 Url")
                )
            ));
    }
}
