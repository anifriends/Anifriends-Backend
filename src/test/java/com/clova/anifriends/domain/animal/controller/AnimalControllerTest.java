package com.clova.anifriends.domain.animal.controller;

import static com.clova.anifriends.domain.animal.support.fixture.AnimalDtoFixture.findAnimalDetail;
import static com.clova.anifriends.domain.animal.support.fixture.AnimalFixture.animal;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.animal.Animal;
import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.request.UpdateAnimalAdoptStatusRequest;
import com.clova.anifriends.domain.animal.dto.request.UpdateAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalNeuteredFilter;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
                ),
                responseFields(
                    fieldWithPath("animalId").type(NUMBER).description("생성된 보호 동물 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호 동물 상세 조회 api 호출 시")
    void FindAnimalDetailTest() throws Exception {
        // given
        long shelterId = 1L;
        Shelter shelter = shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);
        Animal animal = animal(shelter);
        ReflectionTestUtils.setField(animal, "animalId", 1L);
        FindAnimalDetail response = findAnimalDetail(animal);

        when(animalService.findAnimalDetail(shelterId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/animals/{animalId}", shelterId)
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("animalId").description("보호 동물 ID")
                ),
                responseFields(
                    fieldWithPath("animalId").type(NUMBER).description("보호 동물 ID"),
                    fieldWithPath("animalName").type(STRING).description("보호 동물 이름"),
                    fieldWithPath("animalBirthDate").type(STRING).description("보호 동물 출생 날짜"),
                    fieldWithPath("animalType").type(STRING).description("보호 동물 종류"),
                    fieldWithPath("animalBreed").type(STRING).description("보호 동물 품종"),
                    fieldWithPath("animalGender").type(STRING).description("보호 동물 성별"),
                    fieldWithPath("animalIsNeutered").type(BOOLEAN).description("보호 동물 중성화 유무"),
                    fieldWithPath("animalActive").type(STRING).description("보호 동물 활동성"),
                    fieldWithPath("animalWeight").type(NUMBER).description("보호 동물 몸무게"),
                    fieldWithPath("animalInformation").type(STRING).description("보호 동물 기타 정보"),
                    fieldWithPath("animalImageUrls[]").type(ARRAY).description("보호 동물 이미지 url 리스트"),
                    fieldWithPath("animalIsAdopted").type(BOOLEAN).description("보호 동물 입양 여부")
                )
            ));
    }

    @Test
    @DisplayName("보호 동물 조회 & 검색(보호소) api 호출 시")
    void findAnimalsByShelter() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword", "검색어");
        params.add("type", AnimalType.DOG.name());
        params.add("gender", AnimalGender.MALE.name());
        params.add("neuteredFilter", AnimalNeuteredFilter.IS_NEUTERED.name());
        params.add("active", "ACTIVE");
        params.add("animalSize", "SMALL");
        params.add("age", "BABY");
        params.add("page", "0");
        params.add("size", "10");

        Long shelterId = 1L;
        Shelter shelter = shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", shelterId);
        Animal animal = animal(shelter);
        ReflectionTestUtils.setField(animal, "animalId", 1L);

        FindAnimalsByShelterResponse.FindAnimalByShelterResponse findAnimalByShelterResponse = FindAnimalsByShelterResponse.FindAnimalByShelterResponse.from(
            animal);
        PageInfo pageInfo = new PageInfo(1, false);
        FindAnimalsByShelterResponse response = new FindAnimalsByShelterResponse(
            List.of(findAnimalByShelterResponse), pageInfo);

        given(animalService.findAnimalsByShelter(
            any(), anyString(), any(), any(), any(),
            any(), any(), any(), any())).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/shelters/animals")
            .header(AUTHORIZATION, shelterAccessToken)
            .params(params));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("keyword").description("검색어").optional(),
                    parameterWithName("type").description("보호 동물 종류").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("DOG, CAT, ETC")),
                    parameterWithName("gender").description("보호 동물 성별").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("MALE, FEMALE")),
                    parameterWithName("neuteredFilter").description("보호 동물 중성화 여부").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("true, false")),
                    parameterWithName("active").description("보호 동물 성격").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            "QUIET, NORMAL, ACTIVE, VERY_ACTIVE")),
                    parameterWithName("animalSize").description("보호 동물 크기").optional()
                        .attributes(
                            DocumentationFormatGenerator.getConstraint("SMALL, MEDIUM, LARGE")),
                    parameterWithName("age").description("보호 동물 나이").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            "BABY, JUNIOR, ADULT, SENIOR")),
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("size").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("animals").type(ARRAY).description("보호 동물 리스트"),
                    fieldWithPath("animals[].animalId").type(NUMBER).description("보호 동물 ID"),
                    fieldWithPath("animals[].animalName").type(STRING).description("보호 동물 이름"),
                    fieldWithPath("animals[].animalImageUrl").type(STRING).description("보호 동물 사진"),
                    fieldWithPath("animals[].animalBirthDate").type(STRING).description("보호 동물 생일"),
                    fieldWithPath("animals[].animalGender").type(STRING).description("보호 동물 성별"),
                    fieldWithPath("animals[].animalIsAdopted").type(BOOLEAN)
                        .description("보호 동물 입양 여부"),
                    fieldWithPath("animals[].animalIsNeutered").type(BOOLEAN)
                        .description("보호 동물 중성화 유무")
                )
            ));
    }

    @Test
    @DisplayName("보호 동물 조회 & 검색(봉사자) api 호출 시")
    void findAnimals() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", AnimalType.DOG.name());
        params.add("gender", AnimalGender.FEMALE.name());
        params.add("neuteredFilter", AnimalNeuteredFilter.IS_NEUTERED.name());
        params.add("active", AnimalActive.ACTIVE.name());
        params.add("animalSize", AnimalSize.SMALL.name());
        params.add("age", AnimalAge.ADULT.name());
        params.add("page", String.valueOf(0));
        params.add("animalSize", String.valueOf(10));

        Shelter shelter = shelter();
        Animal animal = animal(shelter);
        ReflectionTestUtils.setField(animal, "animalId", 1L);

        FindAnimalsResponse response = FindAnimalsResponse
            .from(new PageImpl<>(List.of(animal)));

        when(animalService.findAnimalsByVolunteer(
            any(AnimalType.class),
            any(AnimalActive.class),
            any(AnimalNeuteredFilter.class),
            any(AnimalAge.class),
            any(AnimalGender.class),
            any(AnimalSize.class),
            any(Pageable.class))
        ).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/animals")
            .header(AUTHORIZATION, volunteerAccessToken)
            .params(params));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("type").description("보호 동물 종류").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalType.values()).map(
                                AnimalType::name).toArray(String[]::new)))),
                    parameterWithName("gender").description("보호 동물 성별").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalGender.values()).map(
                                AnimalGender::name).toArray(String[]::new)))),
                    parameterWithName("neuteredFilter").description("보호 동물 중성화 여부").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("true, false")),
                    parameterWithName("active").description("보호 동물 성격").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalActive.values()).map(
                                AnimalActive::name).toArray(String[]::new)))),
                    parameterWithName("animalSize").description("보호 동물 크기").optional()
                        .attributes(
                            DocumentationFormatGenerator.getConstraint(
                                String.join(", ", Arrays.stream(AnimalSize.values()).map(
                                    AnimalSize::name).toArray(String[]::new)))),
                    parameterWithName("age").description("보호 동물 나이").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalAge.values()).map(
                                AnimalAge::name).toArray(String[]::new)))),
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("animalSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("animals").type(ARRAY).description("보호 동물 리스트"),
                    fieldWithPath("animals[].animalId").type(NUMBER).description("보호 동물 ID"),
                    fieldWithPath("animals[].animalName").type(STRING).description("보호 동물 이름"),
                    fieldWithPath("animals[].shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("animals[].shelterAddress").type(STRING).description("보호소 주소"),
                    fieldWithPath("animals[].animalImageUrl").type(STRING)
                        .description("보호 동물 이미지 url")
                )
            ));
    }

    @Test
    @DisplayName("보호 동물 조회 & 검색(봉사자) api 호출 시")
    void findAnimalsByVolunteerV2() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", AnimalType.DOG.name());
        params.add("gender", AnimalGender.FEMALE.name());
        params.add("neuteredFilter", AnimalNeuteredFilter.IS_NEUTERED.name());
        params.add("active", AnimalActive.ACTIVE.name());
        params.add("animalSize", AnimalSize.SMALL.name());
        params.add("age", AnimalAge.ADULT.name());
        params.add("animalId", String.valueOf(1L));
        params.add("createdAt", String.valueOf(LocalDateTime.now()));
        params.add("page", String.valueOf(0));
        params.add("animalSize", String.valueOf(10));

        Shelter shelter = shelter();
        Animal animal = animal(shelter);
        ReflectionTestUtils.setField(animal, "animalId", 1L);

        FindAnimalsResponse response = FindAnimalsResponse
            .fromV2(new SliceImpl<>(List.of(animal)), 1L);

        when(animalService.findAnimalsByVolunteerV2(
            any(AnimalType.class),
            any(AnimalActive.class),
            any(AnimalNeuteredFilter.class),
            any(AnimalAge.class),
            any(AnimalGender.class),
            any(AnimalSize.class),
            any(LocalDateTime.class),
            anyLong(),
            any(Pageable.class))
        ).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v2/animals")
            .params(params));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                queryParameters(
                    parameterWithName("type").description("보호 동물 종류").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalType.values()).map(
                                AnimalType::name).toArray(String[]::new)))),
                    parameterWithName("gender").description("보호 동물 성별").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalGender.values()).map(
                                AnimalGender::name).toArray(String[]::new)))),
                    parameterWithName("neuteredFilter").description("보호 동물 중성화 여부").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("true, false")),
                    parameterWithName("active").description("보호 동물 성격").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalActive.values()).map(
                                AnimalActive::name).toArray(String[]::new)))),
                    parameterWithName("animalSize").description("보호 동물 크기").optional()
                        .attributes(
                            DocumentationFormatGenerator.getConstraint(
                                String.join(", ", Arrays.stream(AnimalSize.values()).map(
                                    AnimalSize::name).toArray(String[]::new)))),
                    parameterWithName("age").description("보호 동물 나이").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint(
                            String.join(", ", Arrays.stream(AnimalAge.values()).map(
                                AnimalAge::name).toArray(String[]::new)))),
                    parameterWithName("animalId").description("페이지 마지막 보호 동물 ID").optional(),
                    parameterWithName("createdAt").description("페이지 마지막 보호 동물 생일").optional(),
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("animalSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("animals").type(ARRAY).description("보호 동물 리스트"),
                    fieldWithPath("animals[].animalId").type(NUMBER).description("보호 동물 ID"),
                    fieldWithPath("animals[].animalName").type(STRING).description("보호 동물 이름"),
                    fieldWithPath("animals[].shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("animals[].shelterAddress").type(STRING).description("보호소 주소"),
                    fieldWithPath("animals[].animalImageUrl").type(STRING)
                        .description("보호 동물 이미지 url")
                )
            ));
    }

    @Test
    @DisplayName("보호 동물 입양 완료 api 호출 시")
    void updateAnimalAdoptStatus() throws Exception {
        // given
        Shelter shelter = shelter();
        Animal animal = animal(shelter);
        ReflectionTestUtils.setField(animal, "animalId", 1L);

        UpdateAnimalAdoptStatusRequest request = new UpdateAnimalAdoptStatusRequest(
            true);

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/shelters/animals/{animalId}/status", animal.getAnimalId())
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("animalId").description("보호 동물 ID")
                ),
                requestFields(
                    fieldWithPath("isAdopted").type(BOOLEAN).description("보호 동물 입양 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호 동물 수정 api 호출 시")
    void updateAnimal() throws Exception {
        //given
        String mockName = "animalName";
        LocalDate mockBirthDate = LocalDate.now();
        String mockInformation = "animalInformation";
        String mockBreed = "animalBreed";
        List<String> mockImageUrls = List.of("www.aws.s3.com/2");

        AnimalType mockType = AnimalType.DOG;
        AnimalActive mockActive = AnimalActive.ACTIVE;
        Double mockWeight = 1.2;
        Boolean mockIsNeutered = true;
        AnimalGender mockGender = AnimalGender.MALE;

        UpdateAnimalRequest updateAnimalRequest = new UpdateAnimalRequest(mockName, mockBirthDate,
            mockType, mockBreed, mockGender, mockIsNeutered, mockActive, mockWeight,
            mockInformation, mockImageUrls);

        //when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/shelters/animals/{animalId}", 1L)
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAnimalRequest)));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("animalId").description("보호 동물 ID")
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
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호 동물 삭제 api 호출 시")
    void deleteAnimal() throws Exception {
        //given
        Long animalId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/shelters/animals/{animalId}", animalId)
                .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("animalId").description("보호 동물 ID")
                )
            ));
    }
}
