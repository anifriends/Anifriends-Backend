package com.clova.anifriends.domain.applicant.wrapper;

import com.clova.anifriends.domain.common.EnumType;

public enum ApplicantStatus implements EnumType {

    PENDING,
    REFUSED,
    ATTENDANCE,
    NO_SHOW,
    ;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
