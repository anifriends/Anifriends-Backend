package com.clova.anifriends.domain.review.repository.response;

import java.time.LocalDateTime;
import java.util.List;

public interface FindShelterReviewResultV2 {

    Long getReviewId();
    LocalDateTime getCreatedAt();
    String getContent();
    List<String> getReviewImages();
    Long getVolunteerId();
    String getVolunteerName();
    String getVolunteerEmail();
    int getTemperature();
    String getVolunteerImageUrl();
    Long getVolunteerReviewCount();
}
