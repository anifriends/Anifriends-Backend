package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindShelterReviewsByShelterResponse(List<FindShelterReviewResponse> reviews,
                                                  PageInfo pageInfo) {

    public record FindShelterReviewResponse(
        Long reviewId,
        LocalDateTime createdAt,
        String content,
        List<String> reviewImageUrls,
        Long volunteerId,

        String volunteerName,
        int temperature,
        String volunteerImageUrl,
        long VolunteerReviewCount) {

        public static FindShelterReviewResponse from(Review review) {
            Volunteer volunteer = review.getApplicant().getVolunteer();
            return new FindShelterReviewResponse(
                review.getReviewId(),
                review.getCreatedAt(),
                review.getContent(),
                review.getImages(),
                volunteer.getVolunteerId(),
                volunteer.getName(),
                volunteer.getTemperature(),
                volunteer.getVolunteerImageUrl(),
                volunteer.getReviewCount()
            );
        }
    }

    public static FindShelterReviewsByShelterResponse from(Page<Review> reviewPage) {
        PageInfo pageInfo = PageInfo.of(reviewPage.getTotalElements(), reviewPage.hasNext());
        List<FindShelterReviewResponse> reviews = reviewPage
            .map(FindShelterReviewResponse::from)
            .stream().toList();
        return new FindShelterReviewsByShelterResponse(reviews, pageInfo);
    }
}
