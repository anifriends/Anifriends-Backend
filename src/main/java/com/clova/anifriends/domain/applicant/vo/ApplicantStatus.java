package com.clova.anifriends.domain.applicant.vo;

import com.clova.anifriends.domain.common.EnumType;
import java.time.LocalDateTime;

public enum ApplicantStatus implements EnumType {

    PENDING,
    REFUSED,
    ATTENDANCE,
    NOSHOW,
    APPROVED,
    ;

    @Override
    public String getName() {
        return this.name();
    }

    public ApplicantStatus convertToApprovalStatus() {
        if (this == ApplicantStatus.ATTENDANCE || this == ApplicantStatus.NOSHOW) {
            return ApplicantStatus.APPROVED;
        }
        return this;
    }

    public ApplicantStatus convertToApproved(LocalDateTime startTime) {
        if (LocalDateTime.now().isAfter(startTime) && (this == ATTENDANCE || this == NOSHOW)) {
            return this;
        }

        if (LocalDateTime.now().isBefore(startTime) && (this == ATTENDANCE)) {
            return APPROVED;
        }

        return this;
    }
}
