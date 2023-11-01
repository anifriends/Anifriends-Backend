package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;

public record FindRecruitmentResponse(
    String title,
    int capacity,
    int applicantCount,
    String content,
    LocalDateTime startTime,
    LocalDateTime endTime,
    boolean isClosed,
    LocalDateTime deadline,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<String> imageUrls
) {

    public static FindRecruitmentResponse from(Recruitment recruitment) {
        return new FindRecruitmentResponse(
            recruitment.getTitle(),
            recruitment.getCapacity(),
            recruitment.getApplicantCount(),
            recruitment.getContent(),
            recruitment.getStartTime(),
            recruitment.getEndTime(),
            recruitment.isClosed(),
            recruitment.getDeadline(),
            recruitment.getCreatedAt(),
            recruitment.getUpdatedAt(),
            recruitment.getImageUrls()
        );
    }

}
