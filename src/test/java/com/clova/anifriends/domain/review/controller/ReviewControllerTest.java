package com.clova.anifriends.domain.review.controller;

import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.review.support.ReviewDtoFixture.findReviewResponse;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ReviewControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공")
    void findReview() throws Exception {
        //given
        Shelter shelter = shelter();
        Volunteer volunteer = volunteer();
        Recruitment recruitment = recruitment(shelter);
        long reviewId = 1L;
        Review review = review(recruitment, volunteer);
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
                pathParameters(
                    parameterWithName("reviewId").description("리뷰 ID")
                ),
                responseFields(
                    fieldWithPath("content").type(STRING).description("리뷰 내용"),
                    fieldWithPath("shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("shelterImageUrl").type(STRING).description("보호소 이미지 url")
                        .optional(),
                    fieldWithPath("imageUrls[]").type(ARRAY).description("리뷰 이미지 url 리스트")
                )
            ));
    }

}
