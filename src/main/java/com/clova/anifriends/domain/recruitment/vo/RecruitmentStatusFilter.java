package com.clova.anifriends.domain.recruitment.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum RecruitmentStatusFilter implements EnumType {

    IS_OPENED(false),
    IS_CLOSED(true);

    private final Boolean isClosed;

    RecruitmentStatusFilter(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Boolean getIsClosed() {
        return this.isClosed;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
