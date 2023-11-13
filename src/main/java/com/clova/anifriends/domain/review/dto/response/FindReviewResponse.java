package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.review.Review;
import java.util.List;

public record FindReviewResponse(
    Long reviewId,
    String reviewContent,
    List<String> reviewImageUrls
) {

    public static FindReviewResponse from(Review review) {
        return new FindReviewResponse(
            review.getReviewId(),
            review.getContent(),
            review.getImages()
        );
    }
}
