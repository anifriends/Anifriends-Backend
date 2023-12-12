package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse.FindShelterReviewResponse;
import com.clova.anifriends.domain.review.repository.response.FindShelterReviewByShelterResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewMapper {

    public static FindShelterReviewsByShelterResponse resultToResponse(
        Page<FindShelterReviewByShelterResult> result
    ) {
        List<FindShelterReviewResponse> shelterReviews = result.stream()
            .map(shelterReview -> new FindShelterReviewResponse(
                shelterReview.getReviewId(),
                shelterReview.getReviewCreatedAt(),
                shelterReview.getReviewContent(),
                shelterReview.getReviewImageUrls(),
                shelterReview.getVolunteerId(),
                shelterReview.getVolunteerName(),
                shelterReview.getVolunteerTemperature(),
                shelterReview.getVolunteerImageUrl(),
                shelterReview.getVolunteerReviewCount()
            ))
            .toList();
        PageInfo pageInfo = new PageInfo(result.getTotalElements(),
            result.hasNext());
        return new FindShelterReviewsByShelterResponse(shelterReviews, pageInfo);
    }
}
