package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.recruitment.dto.response.RegisterRecruitmentResponse;
import com.clova.anifriends.domain.review.Review;

public record RegisterReviewResponse(Long reviewId) {

    public static RegisterReviewResponse from(Review review) {
        return new RegisterReviewResponse(review.getReviewId());
    }
}
