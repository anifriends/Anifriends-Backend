package com.clova.anifriends.domain.shelter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.dto.FindShelterDetailResponse;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ShelterControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("findShelterDetail 실행 시")
    void findShelterDetail() throws Exception {
        // given
        Long shelterId = 1L;
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);
        ShelterImage shelterImage = ShelterImageFixture.shelterImage(shelter);
        FindShelterDetailResponse findShelterDetailResponse = new FindShelterDetailResponse(
            shelter.getShelterId(),
            shelter.getEmail(),
            shelter.getName(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelterImage.getImageUrl()
        );

        given(shelterService.findShelterDetail(any())).willReturn(findShelterDetailResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/shelters/{shelterId}/profile", shelterId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(print())
            .andDo(restDocs.document(
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
                        .description("보호소 이미지 Url")
                )
            ));
    }
}
