package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.review.ReviewImage;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse.FindShelterReviewResponse;
import com.clova.anifriends.domain.review.repository.response.FindShelterReviewResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewMapper {

    public static FindShelterReviewsByShelterResponse resultToResponse(
        Page<FindShelterReviewResult> findShelterReviewResults
    ) {
        List<FindShelterReviewResponse> shelterReviews = findShelterReviewResults.stream()
            .map(shelterReview -> new FindShelterReviewResponse(
                shelterReview.getReviewId(),
                shelterReview.getCreatedAt(),
                shelterReview.getContent(),
                shelterReview.getReviewImages().stream().map(ReviewImage::getImageUrl).toList(),
                shelterReview.getVolunteerId(),
                shelterReview.getVolunteerName(),
                shelterReview.getTemperature(),
                shelterReview.getVolunteerImageUrl(),
                shelterReview.getVolunteerReviewCount()
            ))
            .toList();
        PageInfo pageInfo = new PageInfo(findShelterReviewResults.getTotalElements(),
            findShelterReviewResults.hasNext());
        return new FindShelterReviewsByShelterResponse(shelterReviews, pageInfo);
    }
}
