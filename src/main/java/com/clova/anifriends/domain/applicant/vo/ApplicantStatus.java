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
        if (LocalDateTime.now().isBefore(startTime)) {
            return REFUSED;
        }

        if (startTime.isBefore(LocalDateTime.now()) && (this == ATTENDANCE || this == NOSHOW)) {
            return this;
        }

        if (startTime.isAfter(LocalDateTime.now()) && (this == ATTENDANCE)) {
            return APPROVED;
        }
        
        return this;
    }
}
