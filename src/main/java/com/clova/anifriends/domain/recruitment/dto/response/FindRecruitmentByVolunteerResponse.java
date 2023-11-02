package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;

public record FindRecruitmentByVolunteerResponse(
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
    List<String> imageUrls,
    FindRecruitmentByVolunteerShelterInfoResponse shelterInfo
) {
    public record FindRecruitmentByVolunteerShelterInfoResponse(
        String shelterName,
        String address,
        String imageUrl,
        String email
    ) {
    }

    public static FindRecruitmentByVolunteerResponse from(Recruitment recruitment) {
        return new FindRecruitmentByVolunteerResponse(
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
            recruitment.getImageUrls(),
            new FindRecruitmentByVolunteerShelterInfoResponse(
                recruitment.getShelter().getName(),
                recruitment.getShelter().getAddress(),
                recruitment.getShelter().getShelterImageUrl(),
                recruitment.getShelter().getEmail()
            )
        );
    }
}
