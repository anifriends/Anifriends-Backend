package com.clova.anifriends.domain.applicant.dto.response;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.common.PageInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindApplyingVolunteersResponse(
    PageInfo pageInfo,
    List<FindApplyingVolunteerResponse> applicants
) {

    public record FindApplyingVolunteerResponse(
        Long shelterId,
        Long recruitmentId,
        Long applicantId,
        String recruitmentTitle,
        String shelterName,
        ApplicantStatus applicantStatus,
        boolean applicantIsWritedReview,
        LocalDateTime recruitmentStartTime
    ) {

    }
}
