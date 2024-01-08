package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindShelterRecruitmentsResponse(
    PageInfo pageInfo,
    List<RecruitmentResponse> recruitments
) {
    private record RecruitmentResponse(
        long recruitmentId,
        String recruitmentTitle,
        LocalDateTime recruitmentStartTime,
        LocalDateTime recruitmentDeadline,
        int recruitmentCapacity,
        int recruitmentApplicantCount
    ) {

        private static RecruitmentResponse from(Recruitment recruitment){
            return new RecruitmentResponse(
                recruitment.getRecruitmentId(),
                recruitment.getTitle(),
                recruitment.getStartTime(),
                recruitment.getDeadline(),
                recruitment.getCapacity(),
                recruitment.getApplicantCount()
            );
        }
    }

    public static FindShelterRecruitmentsResponse from(Page<Recruitment> recruitments) {
        return new FindShelterRecruitmentsResponse(
            PageInfo.of(recruitments.getTotalElements(), recruitments.hasNext()),
            recruitments.stream()
                .map(RecruitmentResponse::from)
                .toList()
        );
    }
}
