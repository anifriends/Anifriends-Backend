package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.util.List;

public record FindRecruitmentResponse(
    String title,
    int capacity,
    int applicantCount,
    String content,
    String startTime,
    String endTime,
    boolean isClosed,
    String deadline,
    String createdAt,
    String updatedAt,
    List<String> imageUrls
) {
    public static FindRecruitmentResponse from(Recruitment recruitment) {
        return new FindRecruitmentResponse(
            recruitment.getTitle(),
            recruitment.getCapacity(),
            recruitment.getApplicantCount(),
            recruitment.getContent(),
            recruitment.getStartTime().toString(),
            recruitment.getEndTime().toString(),
            recruitment.getIsClosed(),
            recruitment.getDeadline().toString(),
            recruitment.getCreatedAt() == null ? null : recruitment.getCreatedAt().toString(),
            recruitment.getUpdatedAt() == null ? null : recruitment.getUpdatedAt().toString(),
            recruitment.getImageUrls()
        );
    }

}
