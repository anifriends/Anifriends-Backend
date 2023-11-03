package com.clova.anifriends.domain.applicant.dto;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record FindApplyingVolunteersResponse(
    List<FindApplyingVolunteerResponse> findApplyingVolunteerResponses
) {

    public record FindApplyingVolunteerResponse(
        Long recruitmentId,
        Long applicantId,
        String title,
        String shelterName,
        ApplicantStatus status,
        boolean isWritedReview,
        LocalDateTime volunteerDate
    ) {

        public static FindApplyingVolunteerResponse from(
            Applicant applicant
        ) {
            if (applicant.getStatus().equals(ApplicantStatus.ATTENDANCE)
                && Objects.isNull(applicant.getReview())) {

                return new FindApplyingVolunteerResponse(
                    applicant.getRecruitment().getRecruitmentId(),
                    applicant.getApplicantId(),
                    applicant.getRecruitment().getTitle(),
                    applicant.getRecruitment().getShelter().getName(),
                    applicant.getStatus(),
                    true,
                    applicant.getRecruitment().getStartTime()
                );
            }

            return new FindApplyingVolunteerResponse(
                applicant.getRecruitment().getRecruitmentId(),
                applicant.getApplicantId(),
                applicant.getRecruitment().getTitle(),
                applicant.getRecruitment().getShelter().getName(),
                applicant.getStatus(),
                false,
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
