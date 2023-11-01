package com.clova.anifriends.domain.recruitment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record RegisterRecruitmentRequest(
    @NotBlank
    String title,
    @NotNull
    LocalDateTime startTime,
    @NotNull
    LocalDateTime endTime,
    @NotNull
    LocalDateTime deadline,
    @NotNull
    Integer capacity,
    @NotBlank
    String content,
    List<String> imageUrls) {

}
