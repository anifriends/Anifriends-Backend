package com.clova.anifriends.domain.recruitment.controller;

import com.clova.anifriends.domain.common.EnumType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KeywordFilter implements EnumType {
    IS_TITLE(true, false, false),
    IS_CONTENT(false, true, false),
    IS_SHELTER_NAME(false, false, true),
    ALL(true, true, true);

    private final boolean titleFilter;
    private final boolean contentFilter;
    private final boolean shelterNameFilter;

    public KeywordCondition getKeywordCondition() {
        return new KeywordCondition(titleFilter, contentFilter, shelterNameFilter);
    }

    public KeywordConditionByShelter getKeywordConditionByShelter() {
        return new KeywordConditionByShelter(titleFilter, contentFilter);
    }

    @Override
    public String getName() {
        return name();
    }
}
