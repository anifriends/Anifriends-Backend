package com.clova.anifriends.domain.applicant.vo;

import com.clova.anifriends.domain.common.EnumType;

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
}
