package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;

public record FindRecruitmentDetailResponse(
    String recruitmentTitle,
    int recruitmentApplicantCount,
    int recruitmentCapacity,
    String recruitmentContent,
    LocalDateTime recruitmentStartTime,
    LocalDateTime recruitmentEndTime,
    boolean recruitmentIsClosed,
    LocalDateTime recruitmentDeadline,
    LocalDateTime recruitmentCreatedAt,
    LocalDateTime recruitmentUpdatedAt,
    List<String> recruitmentImageUrls,
    Long shelterId

) {

    public static FindRecruitmentDetailResponse from(Recruitment recruitment) {
        return new FindRecruitmentDetailResponse(
            recruitment.getTitle(),
            recruitment.getApplicantCount(),
            recruitment.getCapacity(),
            recruitment.getContent(),
            recruitment.getStartTime(),
            recruitment.getEndTime(),
            recruitment.isClosed(),
            recruitment.getDeadline(),
            recruitment.getCreatedAt(),
            recruitment.getUpdatedAt(),
            recruitment.getImages(),
            recruitment.getShelter().getShelterId()
        );
    }
}
