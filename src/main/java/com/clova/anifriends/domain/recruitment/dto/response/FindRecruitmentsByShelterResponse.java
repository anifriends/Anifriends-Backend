package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;

public record FindRecruitmentsByShelterResponse(
    PageInfo pageInfo,
    List<RecruitmentResponse> recruitments

) {
    private record RecruitmentResponse(
       long recruitmentId,
        String title,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime deadline,
        boolean isClosed,
        int applicantCount,
        int capacity
    ) {
        private static RecruitmentResponse from(Recruitment recruitment){
            return new RecruitmentResponse(
                recruitment.getRecruitmentId(),
                recruitment.getTitle(),
                recruitment.getStartTime(),
                recruitment.getEndTime(),
                recruitment.getDeadline(),
                recruitment.isClosed(),
                recruitment.getApplicantCount(),
                recruitment.getCapacity()
            );
        }
    }

    public static FindRecruitmentsByShelterResponse of(List<Recruitment> recruitments, PageInfo pageInfo){
        return new FindRecruitmentsByShelterResponse(
            pageInfo,
            recruitments.stream()
                .map(RecruitmentResponse::from)
                .toList()
        );
    }
}
