package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.review.Review;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindShelterReviewsByShelterResponse(List<FindShelterReviewResponse> reviews,
                                                  PageInfo pageInfo) {

    public record FindShelterReviewResponse(
        Long reviewId,
        LocalDateTime reviewCreatedAt,
        String reviewContent,
        List<String> reviewImageUrls,
        Long volunteerId,
        String volunteerName,
        int volunteerTemperature,
        String volunteerImageUrl,
        long volunteerReviewCount) {

        public static FindShelterReviewResponse from(Review review) {
            return new FindShelterReviewResponse(
                review.getReviewId(),
                review.getCreatedAt(),
                review.getContent(),
                review.getImages(),
                review.getVolunteer().getVolunteerId(),
                review.getVolunteer().getName(),
                review.getVolunteer().getTemperature(),
                review.getVolunteer().getVolunteerImageUrl(),
                review.getVolunteer().getReviewCount()
            );
        }
    }

    public static FindShelterReviewsByShelterResponse from(Page<Review> reviewPage) {
        List<FindShelterReviewResponse> content = reviewPage
            .map(FindShelterReviewResponse::from)
            .toList();
        PageInfo pageInfo = PageInfo.of(reviewPage.getTotalElements(), reviewPage.hasNext());
        return new FindShelterReviewsByShelterResponse(content, pageInfo);
    }
}
