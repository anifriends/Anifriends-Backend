package com.clova.anifriends.domain.applicant.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum ApplicantStatus implements EnumType {

    PENDING,
    REFUSED,
    ATTENDANCE,
    NO_SHOW,
    APPROVED,
    ;

    @Override
    public String getName() {
        return this.name();
    }

    public ApplicantStatus convertToApprovalStatus() {
        if (this == ApplicantStatus.ATTENDANCE || this == ApplicantStatus.NO_SHOW) {
            return ApplicantStatus.APPROVED;
        }
        return this;
    }
}
