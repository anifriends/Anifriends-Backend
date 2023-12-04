package com.clova.anifriends.domain.review.repository.response;

import com.clova.anifriends.domain.review.ReviewImage;
import java.time.LocalDateTime;
import java.util.List;

public interface FindShelterReviewResult {

    Long getReviewId();
    LocalDateTime getCreatedAt();
    String getContent();
    List<ReviewImage> getReviewImages();
    Long getVolunteerId();
    String getVolunteerName();
    String getVolunteerEmail();
    int getTemperature();
    String getVolunteerImageUrl();
    Long getVolunteerReviewCount();
}
