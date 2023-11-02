package com.clova.anifriends.domain.animal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class AnimalControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 보호 동물 등록 api 호출 시")
    void registerAnimal() throws Exception {
        //given
        RegisterAnimalRequest registerAnimalRequest = new RegisterAnimalRequest(
            "name", LocalDate.now(), AnimalType.DOG.getName(), "품종", AnimalGender.FEMALE.getName(),
            false, AnimalActive.QUIET.getName(), 0.7, "기타 정보", List.of("www.aws.s3.com/2"));
        RegisterAnimalResponse registerAnimalResponse = new RegisterAnimalResponse(1L);

        given(animalService.registerAnimal(anyLong(), any())).willReturn(registerAnimalResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/shelters/animals")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerAnimalRequest)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("보호 동물 이름")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                    fieldWithPath("birthDate").type(JsonFieldType.STRING).description("보호 동물 생년월")
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    fieldWithPath("type").type(JsonFieldType.STRING).description("보호 동물 종류")
                        .attributes(DocumentationFormatGenerator.getConstraint("DOG, CAT, ETC")),
                    fieldWithPath("breed").type(JsonFieldType.STRING).description("보호 동물 품종")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                    fieldWithPath("gender").type(JsonFieldType.STRING).description("보호 동물 성별")
                        .attributes(DocumentationFormatGenerator.getConstraint("MALE, FEMALE")),
                    fieldWithPath("isNeutered").type(JsonFieldType.BOOLEAN).description("중성화 여부"),
                    fieldWithPath("active").type(JsonFieldType.STRING).description("성격")
                        .attributes(DocumentationFormatGenerator
                            .getConstraint("QUIET, NORMAL, ACTIVE, VERY_ACTIVE")),
                    fieldWithPath("weight").type(JsonFieldType.NUMBER).description("몸무게")
                        .attributes(DocumentationFormatGenerator.getConstraint("0 이상, 50 이하")),
                    fieldWithPath("information").type(JsonFieldType.STRING).description("기타 정보")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 1000자 이하")),
                    fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("이미지 url 리스트")
                        .attributes(DocumentationFormatGenerator.getConstraint("1장 이상, 5장 이하"))
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스에 접근 가능한 api")
                )
            ));

    }
}