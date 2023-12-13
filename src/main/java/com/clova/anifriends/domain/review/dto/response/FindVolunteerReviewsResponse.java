package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.review.Review;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindVolunteerReviewsResponse(
    PageInfo pageInfo,
    List<ReviewResponse> reviews
) {

    public static FindVolunteerReviewsResponse from(Page<Review> reviewPage) {
        return new FindVolunteerReviewsResponse(
            PageInfo.of(reviewPage.getTotalElements(), reviewPage.hasNext()),
            reviewPage.map(ReviewResponse::from)
                .toList()
        );
    }

    private record ReviewResponse(
        Long reviewId,
        Long shelterId,
        String shelterName,
        LocalDateTime reviewCreatedAt,
        String reviewContent,
        List<String> reviewImageUrls
    ) {

        private static ReviewResponse from(Review review) {
            return new ReviewResponse(
                review.getReviewId(),
                review.getApplicant().getRecruitment().getShelter().getShelterId(),
                review.getApplicant().getRecruitment().getShelter().getName(),
                review.getCreatedAt(),
                review.getContent(),
                review.getImages()
            );
        }
    }
}
