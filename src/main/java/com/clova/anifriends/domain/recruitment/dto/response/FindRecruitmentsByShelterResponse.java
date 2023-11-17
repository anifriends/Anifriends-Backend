package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindRecruitmentsByShelterResponse(
    PageInfo pageInfo,
    List<RecruitmentResponse> recruitments

) {
    private record RecruitmentResponse(
       long recruitmentId,
        String recruitmentTitle,
        LocalDateTime recruitmentStartTime,
        LocalDateTime recruitmentEndTime,
        LocalDateTime recruitmentDeadline,
        boolean recruitmentIsClosed,
        int recruitmentApplicantCount,
        int recruitmentCapacity
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

    public static FindRecruitmentsByShelterResponse from(Page<Recruitment> recruitments){
        return new FindRecruitmentsByShelterResponse(
            PageInfo.from(recruitments),
            recruitments.stream()
                .map(RecruitmentResponse::from)
                .toList()
        );
    }
}
