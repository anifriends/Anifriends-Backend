package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.common.EnumType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecruitmentStatusFilter implements EnumType {

    IS_OPENED(false),
    IS_CLOSED(true),
    ALL(null);

    private final Boolean isClosed;

    public Boolean getIsClosed() {
        return this.isClosed;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
