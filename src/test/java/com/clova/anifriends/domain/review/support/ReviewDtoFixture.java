package com.clova.anifriends.domain.review.support;

import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.request.RegisterReviewRequest;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;

public class ReviewDtoFixture {

    public static FindReviewResponse findReviewResponse(Review review) {
        return FindReviewResponse.from(review);
    }

    public static RegisterReviewRequest registerReviewRequest(Review review) {
        return new RegisterReviewRequest(
            review.getApplicant().getApplicantId(),
            review.getContent(),
            review.getImages()
        );
    }
}
