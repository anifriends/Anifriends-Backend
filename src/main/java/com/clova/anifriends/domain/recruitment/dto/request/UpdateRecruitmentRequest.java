package com.clova.anifriends.domain.recruitment.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateRecruitmentRequest(
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime deadline,
    Integer capacity,
    String content,
    List<String> imageUrls) {

}
