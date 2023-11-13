package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.review.Review;
import java.time.LocalDateTime;
import java.util.List;

public record FindVolunteerReviewsResponse(
    PageInfo pageInfo,
    List<ReviewResponse> reviews
) {

    private record ReviewResponse(
        Long reviewId,
        String shelterName,
        LocalDateTime reviewCreatedAt,
        String reviewContent,
        List<String> reviewImageUrls
    ) {

        private static ReviewResponse from(Review review) {
            return new ReviewResponse(
                review.getReviewId(),
                review.getApplicant().getRecruitment().getShelter().getName(),
                review.getCreatedAt(),
                review.getContent(),
                review.getImages()
            );
        }
    }

    public static FindVolunteerReviewsResponse of(List<Review> reviews, PageInfo pageInfo) {
        return new FindVolunteerReviewsResponse(
            pageInfo,
            reviews.stream()
                .map(ReviewResponse::from)
                .toList()
        );
    }
}
