package com.clova.anifriends.domain.applicant.dto.response;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import java.time.LocalDateTime;
import java.util.List;

public record FindApplyingVolunteersResponse(
    List<FindApplyingVolunteerResponse> findApplyingVolunteerResponses
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
                applicant.getStatus(),
                applicant.hasNotReview(),
                applicant.getRecruitment().getStartTime()
            );
        }
    }

    public static FindApplyingVolunteersResponse from(
        List<Applicant> applicants
    ) {
        return new FindApplyingVolunteersResponse(
            applicants
                .stream()
                .map(FindApplyingVolunteerResponse::from)
                .toList()
        );
    }
}
