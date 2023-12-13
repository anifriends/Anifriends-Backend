package com.clova.anifriends.domain.review.repository.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class FindShelterReviewByShelterResult {

    private Long reviewId;
    private LocalDateTime reviewCreatedAt;
    private String reviewContent;
    private List<String> reviewImageUrls;
    private Long volunteerId;
    private String volunteerName;
    private int volunteerTemperature;
    private String volunteerImageUrl;
    private int volunteerReviewCount;

    public FindShelterReviewByShelterResult(Long reviewId, LocalDateTime reviewCreatedAt,
        String reviewContent, Long volunteerId, String volunteerName,
        int volunteerTemperature, String volunteerImageUrl, int volunteerReviewCount) {
        this.reviewId = reviewId;
        this.reviewCreatedAt = reviewCreatedAt;
        this.reviewContent = reviewContent;
        this.volunteerId = volunteerId;
        this.volunteerName = volunteerName;
        this.volunteerTemperature = volunteerTemperature;
        this.volunteerImageUrl = volunteerImageUrl;
        this.volunteerReviewCount = volunteerReviewCount;
    }

    public void setReviewImageUrls(List<String> reviewImageUrls) {
        this.reviewImageUrls = reviewImageUrls;
    }
}
