package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;

public record FindRecruitmentsByShelterIdResponse(
    PageInfo pageInfo,
    List<RecruitmentResponse> recruitments
) {
    private record RecruitmentResponse(
        String title,
        LocalDateTime volunteerDate,
        LocalDateTime deadline,
        int capacity,
        int applicantCount
    ) {

        private static RecruitmentResponse from(Recruitment recruitment){
            return new RecruitmentResponse(
                recruitment.getTitle(),
                recruitment.getStartTime(),
                recruitment.getDeadline(),
                recruitment.getCapacity(),
                recruitment.getApplicantCount()
            );
        }
    }

    public static FindRecruitmentsByShelterIdResponse of(List<Recruitment> recruitments, PageInfo pageInfo) {
        return new FindRecruitmentsByShelterIdResponse(
            pageInfo,
            recruitments.stream()
                .map(RecruitmentResponse::from)
                .toList()
        );
    }
}
