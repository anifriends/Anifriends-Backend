package com.clova.anifriends.domain.review.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import java.time.LocalDateTime;
import java.util.List;

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
    }

}
