package com.clova.anifriends.domain.recruitment.controller;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture.findRecruitmentsByShelterIdResponse;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
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
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.dto.request.RegisterRecruitmentRequest;
import com.clova.anifriends.domain.recruitment.dto.response.FindCompletedRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentDetailResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterIdResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsByShelterResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse;
import com.clova.anifriends.domain.recruitment.dto.response.FindRecruitmentsResponse.FindRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentDtoFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import com.clova.anifriends.domain.shelter.support.ShelterImageFixture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class RecruitmentControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 봉사 모집글 등록 api 호출")
    void registerRecruitment() throws Exception {
        //given
        String title = "title";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);
        LocalDateTime deadline = now.plusHours(5);
        int capacity = 10;
        String content = "content";
        List<String> imageUrls = new ArrayList<>();
        RegisterRecruitmentRequest request = new RegisterRecruitmentRequest(
            title, startTime, endTime, deadline, capacity, content, imageUrls);
        RegisterRecruitmentResponse response = new RegisterRecruitmentResponse(1L);

        given(recruitmentService.registerRecruitment(anyLong(), anyString(), any(), any(), any(),
            anyInt(), anyString(), anyList())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/shelters/recruitments")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("봉사 모집글 제목")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 50자 이하")),
                    fieldWithPath("startTime").type(JsonFieldType.STRING).description("봉사 시작 시간")
                        .attributes(DocumentationFormatGenerator.getDatetimeConstraint()),
                    fieldWithPath("endTime").type(JsonFieldType.STRING).description("봉사 종료 시간")
                        .attributes(DocumentationFormatGenerator.getDatetimeConstraint()),
                    fieldWithPath("deadline").type(JsonFieldType.STRING).description("봉사 모집 마감 시간")
                        .attributes(DocumentationFormatGenerator.getDatetimeConstraint()),
                    fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("봉사 모집 정원")
                        .attributes(DocumentationFormatGenerator.getConstraint("1명 이상, 99명 이하")),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("봉사 모집글 본문")
                        .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 1000자 이하")),
                    fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("봉사 모집글 이미지")
                        .attributes(DocumentationFormatGenerator.getConstraint("0장 이상, 5장 이하"))
                        .optional()
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스에 대한 접근 api")
                )
            ));
    }

    @Test
    @DisplayName("성공: 봉사자가 완료한 봉사 리스트 조회 API 호출 시")
    void findCompletedRecruitments() throws Exception {
        //given
        Long volunteerId = 1L;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "0");
        params.add("pageSize", "10");
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        ReflectionTestUtils.setField(recruitment, "recruitmentId", 1L);
        PageImpl<Recruitment> recruitments = new PageImpl<>(List.of(recruitment));
        FindCompletedRecruitmentsResponse response = FindCompletedRecruitmentsResponse.from(
            recruitments);

        given(recruitmentService.findCompletedRecruitments(anyLong(), any()))
            .willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/{volunteerId}/recruitments/completed", volunteerId)
                .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("volunteerId").description("봉사자 ID")
                ),
                queryParameters(
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("recruitments").type(ARRAY).description("봉사 모집글 리스트"),
                    fieldWithPath("recruitments[].recruitmentId").type(NUMBER)
                        .description("봉사 모집글 ID"),
                    fieldWithPath("recruitments[].recruitmentTitle").type(STRING)
                        .description("봉사 모집글 제목"),
                    fieldWithPath("recruitments[].recruitmentStartTime").type(STRING)
                        .description("봉사 날짜"),
                    fieldWithPath("recruitments[].shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 봉사 모집글 조회, 검색 API 호출")
    void findRecruitments() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("keyword", "겅색어");
        params.add("startDate", LocalDate.now().toString());
        params.add("endDate", LocalDate.now().toString());
        params.add("isClosed", "false");
        params.add("title", "true");
        params.add("content", "false");
        params.add("shelterName", "false");
        params.add("pageNumber", "0");
        params.add("pageSize", "10");
        Shelter shelter = shelter();
        ShelterImage shelterImage = ShelterImageFixture.shelterImage(shelter);
        shelter.updateShelterImage(shelterImage);
        Recruitment recruitment = recruitment(shelter);
        ReflectionTestUtils.setField(recruitment, "recruitmentId", 1L);
        FindRecruitmentResponse findRecruitmentResponse
            = FindRecruitmentResponse.from(recruitment);
        PageInfo pageInfo = new PageInfo(1, false);
        FindRecruitmentsResponse response = new FindRecruitmentsResponse(
            List.of(findRecruitmentResponse), pageInfo);

        given(recruitmentService.findRecruitments(anyString(), any(), any(),
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), any()))
            .willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/recruitments")
            .header(AUTHORIZATION, volunteerAccessToken)
            .params(params));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("keyword").description("검색어").optional(),
                    parameterWithName("startDate").description("검색 시작일").optional()
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    parameterWithName("endDate").description("검색 종료일").optional()
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    parameterWithName("isClosed").description("마감 여부").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("true, false")),
                    parameterWithName("title").description("제목 포함 검색").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("기본값 true")),
                    parameterWithName("content").description("본문 포함 검색").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("기본값 true")),
                    parameterWithName("shelterName").description("보호소 이름 포함 검색").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("기본값 true")),
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("recruitments").type(ARRAY).description("봉사 모집글 리스트"),
                    fieldWithPath("recruitments[].recruitmentId").type(NUMBER)
                        .description("봉사 모집글 ID"),
                    fieldWithPath("recruitments[].recruitmentTitle").type(STRING)
                        .description("봉사 모집글 제목"),
                    fieldWithPath("recruitments[].recruitmentStartTime").type(STRING)
                        .description("봉사 시작 시간"),
                    fieldWithPath("recruitments[].recruitmentEndTime").type(STRING)
                        .description("봉사 종료 시간"),
                    fieldWithPath("recruitments[].recruitmentIsClosed").type(BOOLEAN)
                        .description("봉사 모집 마감 여부"),
                    fieldWithPath("recruitments[].recruitmentApplicantCount").type(NUMBER)
                        .description("봉사 신청 인원"),
                    fieldWithPath("recruitments[].recruitmentCapacity").type(NUMBER)
                        .description("봉사 정원"),
                    fieldWithPath("recruitments[].shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("recruitments[].shelterImageUrl").type(STRING)
                        .description("보호소 이미지 url"),
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("findRecruitmentsByShelter 실행 시")
    void findRecruitmentsByShelter() throws Exception {
        // given
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        setField(recruitment, "recruitmentId", 1L);
        Page<Recruitment> pageResult = new PageImpl<>(List.of(recruitment));
        FindRecruitmentsByShelterResponse response = RecruitmentDtoFixture.findRecruitmentsByShelterResponse(
            pageResult);

        when(recruitmentService.findRecruitmentsByShelter(anyLong(), any(), any(), any(),
            anyBoolean(), anyBoolean(), any()))
            .thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/recruitments")
                .header(AUTHORIZATION, shelterAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(headerWithName(AUTHORIZATION).description("액세스 토큰")),
                queryParameters(
                    parameterWithName("keyword").description("검색어").optional(),
                    parameterWithName("startDate").description("검색 시작 날짜").optional()
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    parameterWithName("endDate").description("검색 종료 날짜").optional()
                        .attributes(DocumentationFormatGenerator.getDateConstraint()),
                    parameterWithName("content").description("내용 검색 여부").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("기본값 null")),
                    parameterWithName("title").description("제목 검색 여부").optional()
                        .attributes(DocumentationFormatGenerator.getConstraint("기본값 null")),
                    parameterWithName("pageSize").description("페이지 크기").optional(),
                    parameterWithName("pageNumber").description("페이지 번호").optional()
                ),
                responseFields(
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 게시글 수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("recruitments[]").type(ARRAY).description("모집 게시글 리스트"),
                    fieldWithPath("recruitments[].recruitmentId").type(NUMBER).description("모집 ID"),
                    fieldWithPath("recruitments[].recruitmentTitle").type(STRING)
                        .description("모집 제목"),
                    fieldWithPath("recruitments[].recruitmentStartTime").type(STRING)
                        .description("봉사 시작 시간"),
                    fieldWithPath("recruitments[].recruitmentEndTime").type(STRING)
                        .description("봉사 끝난 시간"),
                    fieldWithPath("recruitments[].recruitmentDeadline").type(STRING)
                        .description("모집 마감 시간"),
                    fieldWithPath("recruitments[].recruitmentIsClosed").type(BOOLEAN)
                        .description("모집 마감 여부"),
                    fieldWithPath("recruitments[].recruitmentApplicantCount").type(NUMBER)
                        .description("현재 지원자 수"),
                    fieldWithPath("recruitments[].recruitmentCapacity").type(NUMBER)
                        .description("모집 정원")
                )
            ));
    }

    @Test
    @DisplayName("findRecruitmentsByShelterId 메서드 실행 시")
    void findRecruitmentsByShelterId() throws Exception {
        // given
        Shelter shelter = shelter();
        setField(shelter, "shelterId", 1L);
        Recruitment recruitment = recruitment(shelter);
        setField(recruitment, "recruitmentId", 1L);
        Page<Recruitment> pageResult = new PageImpl<>(List.of(recruitment));
        FindRecruitmentsByShelterIdResponse response = findRecruitmentsByShelterIdResponse(
            pageResult);

        when(recruitmentService.findShelterRecruitmentsByShelter(anyLong(), any()))
            .thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/shelters/{shelterId}/recruitments", shelter.getShelterId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("shelterId").description("보호소 ID")
                ),
                queryParameters(
                    parameterWithName("pageSize").description("페이지 크기").optional(),
                    parameterWithName("pageNumber").description("페이지 번호").optional()
                ),
                responseFields(
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 게시글 수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("recruitments[]").type(ARRAY).description("모집 게시글 리스트"),
                    fieldWithPath("recruitments[].recruitmentId").type(NUMBER).description("모집 ID"),
                    fieldWithPath("recruitments[].recruitmentTitle").type(STRING)
                        .description("모집 제목"),
                    fieldWithPath("recruitments[].recruitmentStartTime").type(STRING)
                        .description("봉사 시작 시간"),
                    fieldWithPath("recruitments[].recruitmentDeadline").type(STRING)
                        .description("모집 마감 시간"),
                    fieldWithPath("recruitments[].recruitmentCapacity").type(NUMBER)
                        .description("모집 정원"),
                    fieldWithPath("recruitments[].recruitmentApplicantCount").type(NUMBER)
                        .description("현재 지원자 수")
                )
            ));
    }

    @Test
    @DisplayName("findRecruitmentDetail 메서드 실행 시")
    void findRecruitmentDetail() throws Exception {
        // given
        Shelter shelter = shelter();
        setField(shelter, "shelterId", 1L);
        Recruitment recruitment = recruitment(shelter);
        setField(recruitment, "recruitmentId", 1L);
        FindRecruitmentDetailResponse response = RecruitmentDtoFixture.findRecruitmentDetailResponse(
            recruitment);

        when(recruitmentService.findRecruitmentDetail(recruitment.getRecruitmentId())).thenReturn(
            response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/recruitments/{recruitmentId}", recruitment.getRecruitmentId())
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("recruitmentId").description("봉사 모집글 ID")
                ),
                responseFields(
                    fieldWithPath("recruitmentTitle").type(STRING).description("모집글 제목"),
                    fieldWithPath("recruitmentApplicantCount").type(NUMBER)
                        .description("모집글 지원자 수"),
                    fieldWithPath("recruitmentCapacity").type(NUMBER).description("모집글 정원"),
                    fieldWithPath("recruitmentContent").type(STRING).description("모집글 내용"),
                    fieldWithPath("recruitmentStartTime").type(STRING).description("봉사 시작 시간"),
                    fieldWithPath("recruitmentEndTime").type(STRING).description("봉사 종료 시간"),
                    fieldWithPath("recruitmentIsClosed").type(BOOLEAN).description("마감 여부"),
                    fieldWithPath("recruitmentDeadline").type(STRING).description("마감 시간"),
                    fieldWithPath("recruitmentCreatedAt").type(STRING).description("모집글 생성 시간")
                        .optional(),
                    fieldWithPath("recruitmentUpdatedAt").type(STRING).description("모집글 업데이트 시간")
                        .optional(),
                    fieldWithPath("recruitmentImageUrls").type(ARRAY).description("모집글 이미지 url 리스트")
                )
            ));
    }

    @Test
    @DisplayName("성공: 봉사 모집글 마감(보호소) api 호출 시")
    void closeRecruitment() throws Exception {
        //given
        long recruitmentId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/shelters/recruitments/{recruitmentId}/close", recruitmentId)
                .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("recruitmentId").description("봉사 모집글 ID")
                )
            ));
    }
}
