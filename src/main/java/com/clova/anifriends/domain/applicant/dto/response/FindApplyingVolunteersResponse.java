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

        public static FindApplyingVolunteerResponse from(
            Applicant applicant
        ) {
            return new FindApplyingVolunteerResponse(
                applicant.getRecruitment().getShelter().getShelterId(),
                applicant.getRecruitment().getRecruitmentId(),
                applicant.getApplicantId(),
                applicant.getRecruitment().getTitle(),
                applicant.getRecruitment().getShelter().getName(),
                applicant.getStatus().convertToApproved(applicant.getRecruitment().getStartTime()),
                applicant.hasNotReview(),
                applicant.getRecruitment().getStartTime()
            );
        }
    }

    public static FindApplyingVolunteersResponse from(
        Page<Applicant> pagination
    ) {
        PageInfo pageInfo = PageInfo.of(pagination.getTotalElements(), pagination.hasNext());

        List<FindApplyingVolunteerResponse> findApplyingVolunteerResponses = pagination.get()
            .map(FindApplyingVolunteerResponse::from)
            .toList();

        return new FindApplyingVolunteersResponse(pageInfo, findApplyingVolunteerResponses);
    }
}
