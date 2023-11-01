package com.clova.anifriends.domain.recruitment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record RegisterRecruitmentRequest(
    @NotBlank(message = "봉사 모집글 제목은 필수값입니다.")
    String title,
    @NotNull(message = "봉사 시작 시간은 필수값입니다.")
    LocalDateTime startTime,
    @NotNull(message = "봉사 종료 시간은 필수값입니다.")
    LocalDateTime endTime,
    @NotNull(message = "봉사 모집 마갑 시간은 필수값입니다.")
    LocalDateTime deadline,
    @NotNull(message = "봉사 정원은 필수값입니다.")
    Integer capacity,
    @NotBlank(message = "봉사 모집글 본문은 필수값입니다.")
    String content,
    List<String> imageUrls) {

}
