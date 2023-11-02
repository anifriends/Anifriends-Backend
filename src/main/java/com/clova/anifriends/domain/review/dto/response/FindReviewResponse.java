package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.review.Review;
import java.util.List;

public record FindReviewResponse(
    String content,
    List<String> imageUrls,
    String shelterName,
    String shelterImageUrl
) {

    public static FindReviewResponse from(Review review) {
        return new FindReviewResponse(
            review.getContent(),
            review.getImageUrls(),
            review.getRecruitment().getShelter().getName(),
            review.getRecruitment().getShelter().getImageUrl()
        );
    }
}
