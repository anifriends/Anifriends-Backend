package com.clova.anifriends.domain.animal.controller;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalDtoFixture.findAnimalByVolunteerResponse;
import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animal;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.shelter.Shelter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class AnimalControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("findAnimalByIdByVolunteer 실행 시")
    void FindAnimalTest() throws Exception {
        // given
        long shelterId = 1L;
        Shelter shelter = shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);
        Animal animal = animal(shelter);
        FindAnimalByVolunteerResponse response = findAnimalByVolunteerResponse(animal);

        when(animalService.findAnimalByIdByVolunteer(shelterId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/volunteers/animals/{animalId}", shelterId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("animalId").description("보호 동물 ID")
                ),
                responseFields(
                    fieldWithPath("name").type(STRING).description("보호 동물 이름"),
                    fieldWithPath("birthDate").type(STRING).description("출생 날짜"),
                    fieldWithPath("breed").type(STRING).description("품종"),
                    fieldWithPath("gender").type(STRING).description("성별"),
                    fieldWithPath("isNeutered").type(BOOLEAN).description("중성화 유무"),
                    fieldWithPath("active").type(STRING).description("활동성"),
                    fieldWithPath("weight").type(NUMBER).description("몸무게"),
                    fieldWithPath("information").type(STRING).description("기타 정보"),
                    fieldWithPath("imageUrls[]").type(ARRAY).description("이미지 url 리스트"),
                    fieldWithPath("shelter.shelterId").type(NUMBER).description("보호소 ID"),
                    fieldWithPath("shelter.name").type(STRING).description("보호소 이름"),
                    fieldWithPath("shelter.imageUrl").type(STRING).description("보호소 이미지 url")
                        .optional(),
                    fieldWithPath("shelter.email").type(STRING).description("보호소 이메일"),
                    fieldWithPath("shelter.address").type(STRING).description("보호소 주소")
                )
            ));
    }

}
