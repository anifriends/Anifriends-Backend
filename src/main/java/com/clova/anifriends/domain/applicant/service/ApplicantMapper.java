package com.clova.anifriends.domain.applicant.service;

import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse.FindApplyingVolunteerResponse;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicantMapper {

    public static FindApplyingVolunteersResponse resultToResponse(
        List<FindApplyingVolunteerResult> findApplyingVolunteers) {
        List<FindApplyingVolunteerResponse> responses = findApplyingVolunteers.stream()
            .map(result -> new FindApplyingVolunteerResponse(
                result.getShelterId(),
                result.getRecruitmentId(),
                result.getApplicantId(),
                result.getRecruitmentTitle(),
                result.getShelterName(),
                result.getApplicantStatus(),
                result.getApplicantIsWritedReview(),
                result.getRecruitmentStartTime()))
            .toList();
        return new FindApplyingVolunteersResponse(responses);
    }
}
