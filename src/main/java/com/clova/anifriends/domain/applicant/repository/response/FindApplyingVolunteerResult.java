package com.clova.anifriends.domain.applicant.repository.response;

import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import java.time.LocalDateTime;

public interface FindApplyingVolunteerResult {

    Long getShelterId();
    Long getRecruitmentId();
    Long getApplicantId();
    String getRecruitmentTitle();
    String getShelterName();
    ApplicantStatus getApplicantStatus();
    boolean getApplicantIsWritedReview();
    LocalDateTime getRecruitmentStartTime();
}
