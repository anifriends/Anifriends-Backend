package com.clova.anifriends.domain.animal.controller;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalDtoFixture.findAnimalByVolunteerResponse;
import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animal;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
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
                    fieldWithPath("name").type(STRING).description("보호 동물 이름")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                    fieldWithPath("birthDate").type(STRING).description("보호 동물 생년월")
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    fieldWithPath("type").type(STRING).description("보호 동물 종류")
                        .attributes(DocumentationFormatGenerator.getConstraint("DOG, CAT, ETC")),
                    fieldWithPath("breed").type(STRING).description("보호 동물 품종")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                    fieldWithPath("gender").type(STRING).description("보호 동물 성별")
                        .attributes(DocumentationFormatGenerator.getConstraint("MALE, FEMALE")),
                    fieldWithPath("isNeutered").type(BOOLEAN).description("중성화 여부"),
                    fieldWithPath("active").type(STRING).description("성격")
                        .attributes(DocumentationFormatGenerator
                            .getConstraint("QUIET, NORMAL, ACTIVE, VERY_ACTIVE")),
                    fieldWithPath("weight").type(NUMBER).description("몸무게")
                        .attributes(DocumentationFormatGenerator.getConstraint("0 이상, 50 이하")),
                    fieldWithPath("information").type(STRING).description("기타 정보")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 1000자 이하")),
                    fieldWithPath("imageUrls").type(ARRAY).description("이미지 url 리스트")
                        .attributes(DocumentationFormatGenerator.getConstraint("1장 이상, 5장 이하"))
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스에 접근 가능한 api")
                )
            ));
    }

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

    @Test
    @DisplayName("보호 동물 상세 조회(보호소) api 호출 시")
    void findAnimalByShelter() throws Exception {
        //given
        Long animalId = 1L;
        FindAnimalByShelterResponse response = new FindAnimalByShelterResponse(
            animalId,
            "name",
            LocalDate.now().minusYears(1),
            AnimalType.DOG,
            "요크셔테리어",
            AnimalGender.MALE,
            false,
            AnimalActive.ACTIVE,
            2.7,
            "기타 정보",
            false,
            List.of("www.aws.s3.com/2", "www.aws.s3.com/2")
        );

        given(animalService.findAnimalByShelter(anyLong())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/api/shelters/animals/{animalId}", animalId)
                .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("animalId").description("보호 동물 ID")
                ),
                responseFields(
                    fieldWithPath("animalId").type(NUMBER).description("보호 동물 ID"),
                    fieldWithPath("name").type(STRING).description("보호 동물 이름"),
                    fieldWithPath("birthDate").type(STRING).description("출생 날짜"),
                    fieldWithPath("type").type(STRING).description("종류"),
                    fieldWithPath("breed").type(STRING).description("품종"),
                    fieldWithPath("gender").type(STRING).description("성별"),
                    fieldWithPath("isNeutered").type(BOOLEAN).description("중성화 유무"),
                    fieldWithPath("active").type(STRING).description("활동성"),
                    fieldWithPath("weight").type(NUMBER).description("몸무게"),
                    fieldWithPath("information").type(STRING).description("기타 정보"),
                    fieldWithPath("isAdopted").type(BOOLEAN).description("입양 여부"),
                    fieldWithPath("imageUrls[]").type(ARRAY).description("이미지 url 리스트")
                )
            ));
    }
}
