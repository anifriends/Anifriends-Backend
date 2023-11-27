package com.clova.anifriends.domain.review.controller;


import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.review.support.ReviewDtoFixture.findReviewResponse;
import static com.clova.anifriends.domain.review.support.ReviewDtoFixture.registerReviewRequest;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.request.RegisterReviewRequest;
import com.clova.anifriends.domain.review.dto.request.UpdateReviewRequest;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.RegisterReviewResponse;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ReviewControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 리뷰 상세 조회 api 호출")
    void findReview() throws Exception {
        //given
        Shelter shelter = shelter();
        Volunteer volunteer = volunteer();
        Recruitment recruitment = recruitment(shelter);
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        long reviewId = 1L;
        Review review = review(applicant);
        ReflectionTestUtils.setField(review, "reviewId", reviewId);
        FindReviewResponse response = findReviewResponse(review);

        when(reviewService.findReview(anyLong(), eq(reviewId)))
            .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
            get("/api/volunteers/reviews/{reviewId}", reviewId)
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("reviewId").description("리뷰 ID")
                ),
                responseFields(
                    fieldWithPath("reviewId").type(NUMBER).description("리뷰 ID"),
                    fieldWithPath("reviewContent").type(STRING).description("리뷰 내용"),
                    fieldWithPath("reviewImageUrls[]").type(ARRAY).description("리뷰 이미지 url 리스트")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호소가 받은 봉사자 리뷰 목록 조회 api 호출")
    void findShelterReviewsByShelter() throws Exception {
        //give
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        Volunteer volunteer = volunteer();
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        Review review = review(applicant);
        ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
        ReflectionTestUtils.setField(review, "reviewId", 1L);
        ReflectionTestUtils.setField(review, "createdAt", LocalDateTime.now());
        PageImpl<Review> reviewPage = new PageImpl<>(List.of(review));
        FindShelterReviewsByShelterResponse response = FindShelterReviewsByShelterResponse.from(
            reviewPage);

        given(reviewService.findShelterReviewsByShelter(anyLong(), any())).willReturn(response);

        //when
        ResultActions resultActions
            = mockMvc.perform(get("/api/shelters/me/reviews")
            .header(AUTHORIZATION, shelterAccessToken)
            .param("pageNumber", "0")
            .param("pageSize", "10"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("reviews").type(ARRAY).description("리뷰 리스트"),
                    fieldWithPath("reviews[].reviewId").type(NUMBER).description("리뷰 ID"),
                    fieldWithPath("reviews[].createdAt").type(STRING).description("리뷰 생성일"),
                    fieldWithPath("reviews[].content").type(STRING).description("리뷰 내용"),
                    fieldWithPath("reviews[].reviewImageUrls").type(ARRAY)
                    .description("리뷰 이미지 url 리스트").optional(),
                    fieldWithPath("reviews[].volunteerId").type(NUMBER).description("봉사자 ID"),
                    fieldWithPath("reviews[].volunteerName").type(STRING).description("봉사자 이름"),
                    fieldWithPath("reviews[].temperature").type(NUMBER).description("봉사자 온도"),
                    fieldWithPath("reviews[].volunteerImageUrl").type(STRING)
                        .description("봉사자 프로필 이미지 url").optional(),
                    fieldWithPath("reviews[].VolunteerReviewCount").type(NUMBER)
                        .description("봉사자 리뷰 수"),
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 리뷰 등록 api 호출")
    void registerReview() throws Exception {
        //given
        long reviewId = 1L;
        Applicant applicant = applicant(recruitment(shelter()), volunteer(), ATTENDANCE);
        ReflectionTestUtils.setField(applicant, "applicantId", 1L);
        Review review = review(applicant);
        ReflectionTestUtils.setField(review, "reviewId", reviewId);
        RegisterReviewRequest request = registerReviewRequest(review(applicant));
        RegisterReviewResponse registerReviewResponse = new RegisterReviewResponse(reviewId);

        when(reviewService.registerReview(anyLong(), eq(applicant.getApplicantId()),
            eq(review.getContent()), eq(review.getImages())))
            .thenReturn(registerReviewResponse);

        //when
        ResultActions result = mockMvc.perform(
            post("/api/volunteers/reviews")
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("applicationId").type(NUMBER).description("봉사 신청 ID"),
                    fieldWithPath("content").type(STRING).description("리뷰 내용")
                        .description(DocumentationFormatGenerator.getConstraint("10 ~ 300자")),
                    fieldWithPath("imageUrls[]").type(ARRAY).description("리뷰 이미지 url 리스트")
                        .description(DocumentationFormatGenerator.getConstraint("최대 5개"))
                        .optional()
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 리소스에 대한 접근 api")
                ),
                responseFields(
                    fieldWithPath("reviewId").type(NUMBER).description("생성된 리뷰 ID")
                )
            ));
    }


    @Test
    @DisplayName("성공: 봉사자가 작성한 후기 리스트 조회(보호소) api 호출")
    void findVolunteerReviewsByShelter() throws Exception {
        // given
        Long volunteerId = 1L;
        Volunteer volunteer = volunteer();
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        Review review = review(applicant);
        setField(review, "reviewId", 1L);
        setField(review, "createdAt", LocalDateTime.now());
        Page<Review> page = new PageImpl<>(List.of(review));
        FindVolunteerReviewsResponse response = FindVolunteerReviewsResponse.of(
            page.getContent(), PageInfo.from(page));

        given(reviewService.findVolunteerReviews(anyLong(), any())).willReturn(response);

        // when
        ResultActions resultActions
            = mockMvc.perform(get("/api/shelters/volunteers/{volunteerId}/reviews", volunteerId)
            .header(AUTHORIZATION, shelterAccessToken)
            .param("pageNumber", "0")
            .param("pageSize", "10"));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("volunteerId").description("봉사자 ID")
                ),
                queryParameters(
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                    fieldWithPath("reviews").type(ARRAY).description("리뷰 리스트"),
                    fieldWithPath("reviews[].reviewId").type(NUMBER).description("리뷰 ID"),
                    fieldWithPath("reviews[].shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("reviews[].reviewCreatedAt").type(STRING).description("리뷰 생성일"),
                    fieldWithPath("reviews[].reviewContent").type(STRING).description("리뷰 내용"),
                    fieldWithPath("reviews[].reviewImageUrls").type(ARRAY)
                        .description("리뷰 이미지 url 리스트")
                )
            ));
    }


    @Test
    @DisplayName("성공: 봉사자가 작성한 후기 리스트 조회(봉사자) api 호출")
    void findVolunteerReviewsByVolunteers() throws Exception {
        // given
        Volunteer volunteer = volunteer();
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        Review review = review(applicant);
        setField(review, "reviewId", 1L);
        setField(review, "createdAt", LocalDateTime.now());
        Page<Review> page = new PageImpl<>(List.of(review));
        FindVolunteerReviewsResponse response = FindVolunteerReviewsResponse.of(
            page.getContent(), PageInfo.from(page));

        given(reviewService.findVolunteerReviews(anyLong(), any())).willReturn(response);

        // when
        ResultActions resultActions
            = mockMvc.perform(get("/api/volunteers/me/reviews")
            .header(AUTHORIZATION, volunteerAccessToken)
            .param("pageNumber", "0")
            .param("pageSize", "10"));

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
                    fieldWithPath("reviews").type(ARRAY).description("리뷰 리스트"),
                    fieldWithPath("reviews[].reviewId").type(NUMBER).description("리뷰 ID"),
                    fieldWithPath("reviews[].shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("reviews[].reviewCreatedAt").type(STRING).description("리뷰 생성일"),
                    fieldWithPath("reviews[].reviewContent").type(STRING).description("리뷰 내용"),
                    fieldWithPath("reviews[].reviewImageUrls").type(ARRAY)
                        .description("리뷰 이미지 url 리스트")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호소가 받은 봉사자 리뷰 목록 조회 api 호출")
    void findShelterReviews() throws Exception {
        //given
        Long shelterId = 1L;
        Shelter shelter = shelter();
        Recruitment recruitment = recruitment(shelter);
        Volunteer volunteer = volunteer();
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        Review review = review(applicant);
        ReflectionTestUtils.setField(review, "reviewId", 1L);
        ReflectionTestUtils.setField(review, "createdAt", LocalDateTime.now());
        PageImpl<Review> reviewPage = new PageImpl<>(List.of(review));
        FindShelterReviewsResponse response = FindShelterReviewsResponse.from(
            reviewPage);

        given(reviewService.findShelterReviews(anyLong(), any())).willReturn(response);

        //when
        ResultActions resultActions
            = mockMvc.perform(get("/api/shelters/{shelterId}/reviews", shelterId)
            .param("pageNumber", "0")
            .param("pageSize", "10"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("shelterId").description("보호소 ID")
                ),
                queryParameters(
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("reviews").type(ARRAY).description("리뷰 리스트"),
                    fieldWithPath("reviews[].reviewId").type(NUMBER).description("리뷰 ID"),
                    fieldWithPath("reviews[].reviewCreatedAt").type(STRING).description("리뷰 생성일"),
                    fieldWithPath("reviews[].reviewContent").type(STRING).description("리뷰 내용"),
                    fieldWithPath("reviews[].reviewImageUrls").type(ARRAY)
                        .description("리뷰 이미지 url 리스트"),
                    fieldWithPath("reviews[].volunteerEmail").type(STRING).description("봉사자 이메일"),
                    fieldWithPath("reviews[].volunteerTemperature").type(NUMBER)
                        .description("봉사자 온도"),
                    fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 리뷰 수정 api 호출")
    void updateReview() throws Exception {
        // given
        String content = "글 내용";
        List<String> imageUrls = List.of("url1", "url2");
        UpdateReviewRequest updateReviewRequest = new UpdateReviewRequest(content, imageUrls);

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/volunteers/reviews/{reviewId}", 1L)
                .header(AUTHORIZATION, volunteerAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReviewRequest))
        );

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("reviewId").description("리뷰 ID")
                ),
                requestFields(
                    fieldWithPath("content").type(STRING).description("리뷰 내용")
                        .description(DocumentationFormatGenerator.getConstraint("10 ~ 300자")),
                    fieldWithPath("imageUrls[]").type(ARRAY).description("리뷰 이미지 url 리스트")
                        .description(DocumentationFormatGenerator.getConstraint("최대 5개"))
                        .optional()
                )
            ));
    }

    @Test
    @DisplayName("성공: 봉사 후기 삭제 api 호출 시")
    void deleteReview() throws Exception {
        //given
        Long reviewId = 1L;

        //when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/volunteers/reviews/{reviewId}", reviewId)
                .header(AUTHORIZATION, volunteerAccessToken));

        //then
        resultActions.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("reviewId").description("봉사 후기 ID")
                )
            ));
    }
}
