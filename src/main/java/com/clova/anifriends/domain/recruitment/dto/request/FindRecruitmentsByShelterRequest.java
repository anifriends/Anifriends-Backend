package com.clova.anifriends.domain.recruitment.dto.request;

import com.clova.anifriends.domain.recruitment.controller.KeywordFilter;
import java.time.LocalDate;

public record FindRecruitmentsByShelterRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isClosed,
    KeywordFilter keywordFilter
) {

    public FindRecruitmentsByShelterRequest(String keyword, LocalDate startDate, LocalDate endDate,
        Boolean isClosed, KeywordFilter keywordFilter) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isClosed = isClosed;
        this.keywordFilter = keywordFilter == null ? KeywordFilter.ALL : keywordFilter;
    }
}
