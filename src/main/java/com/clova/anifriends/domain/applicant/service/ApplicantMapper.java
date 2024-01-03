package com.clova.anifriends.domain.applicant.service;

import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse.FindApplicantResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse.FindApplyingVolunteerResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApprovedApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApprovedApplicantsResponse.FindApprovedApplicantResponse;
import com.clova.anifriends.domain.applicant.repository.response.FindApplicantResult;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import com.clova.anifriends.domain.applicant.repository.response.FindApprovedApplicantsResult;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.recruitment.Recruitment;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicantMapper {

    public static FindApplyingVolunteersResponse resultToResponse(
        Page<FindApplyingVolunteerResult> findApplyingVolunteers) {
        List<FindApplyingVolunteerResponse> responses = findApplyingVolunteers.stream()
            .map(result -> new FindApplyingVolunteerResponse(
                result.getShelterId(),
                result.getRecruitmentId(),
                result.getApplicantId(),
                result.getRecruitmentTitle(),
                result.getShelterName(),
                result.getApplicantStatus().convertToApproved(result.getRecruitmentStartTime()),
                result.getApplicantIsWritedReview(),
                result.getRecruitmentStartTime()))
            .toList();
        PageInfo pageInfo = PageInfo.of(findApplyingVolunteers.getTotalElements(),
            findApplyingVolunteers.hasNext());
        return new FindApplyingVolunteersResponse(pageInfo, responses);
    }

    public static FindApplicantsResponse resultToResponse(
        List<FindApplicantResult> findApplicants, Recruitment recruitment) {
        List<FindApplicantResponse> responses = findApplicants.stream()
            .map(result -> new FindApplicantResponse(
                result.getVolunteerId(),
                result.getApplicantId(),
                result.getVolunteerName(),
                result.getVolunteerBirthDate(),
                result.getVolunteerGender(),
                result.getCompletedVolunteerCount(),
                result.getVolunteerTemperature(),
                result.getApplicantStatus().convertToApprovalStatus().name()
            ))
            .toList();
        return new FindApplicantsResponse(responses, recruitment.getCapacity());
    }

    public static FindApprovedApplicantsResponse resultToResponse(
        List<FindApprovedApplicantsResult> applicantsApproved
    ) {
        List<FindApprovedApplicantResponse> response = applicantsApproved.stream()
            .map(findApprovedApplicantsResult -> new FindApprovedApplicantResponse(
                    findApprovedApplicantsResult.getVolunteerId(),
                    findApprovedApplicantsResult.getApplicantId(),
                    findApprovedApplicantsResult.getVolunteerName(),
                    findApprovedApplicantsResult.getVolunteerBirthDate(),
                    findApprovedApplicantsResult.getVolunteerGender(),
                    findApprovedApplicantsResult.getVolunteerPhoneNumber(),
                    findApprovedApplicantsResult.getApplicantStatus().equals(ApplicantStatus.ATTENDANCE)
                )
            )
            .toList();

        return new FindApprovedApplicantsResponse(response);
    }
}
