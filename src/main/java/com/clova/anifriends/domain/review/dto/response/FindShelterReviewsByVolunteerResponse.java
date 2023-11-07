package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.review.Review;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindShelterReviewsByVolunteerResponse(
    List<FindShelterReviewByVolunteerResponse> reviews,
    PageInfo pageInfo
) {

    public record FindShelterReviewByVolunteerResponse(
        Long reviewId,
        Integer temperature,
        LocalDateTime createdAt,
        String content,
        String email,
        List<String> reviewImageUrls
    ) {

        public static FindShelterReviewByVolunteerResponse from(
            Review review
        ) {
            return new FindShelterReviewByVolunteerResponse(
                review.getReviewId(),
                review.getApplicant().getVolunteer().getTemperature(),
                review.getCreatedAt(),
                review.getContent(),
                review.getApplicant().getVolunteer().getEmail(),
                review.getImageUrls()
            );
        }
    }

    public static FindShelterReviewsByVolunteerResponse from(
        Page<Review> reviewPage
    ) {
        PageInfo pageInfo = PageInfo.of(reviewPage.getTotalElements(), reviewPage.hasNext());

        List<FindShelterReviewByVolunteerResponse> reviews = reviewPage
            .map(FindShelterReviewByVolunteerResponse::from)
            .stream()
            .toList();

        return new FindShelterReviewsByVolunteerResponse(reviews, pageInfo);
    }
}