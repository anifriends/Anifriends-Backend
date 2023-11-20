package com.clova.anifriends.domain.recruitment.dto.request;

import com.clova.anifriends.domain.recruitment.controller.KeywordFilter;
import com.clova.anifriends.domain.recruitment.controller.RecruitmentStatusFilter;
import java.time.LocalDate;

public record FindRecruitmentsRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    RecruitmentStatusFilter closedFilter,
    KeywordFilter keywordFilter
) {

    public FindRecruitmentsRequest(
        String keyword,
        LocalDate startDate,
        LocalDate endDate,
        RecruitmentStatusFilter closedFilter,
        KeywordFilter keywordFilter) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closedFilter = closedFilter == null ? RecruitmentStatusFilter.ALL : closedFilter;
        this.keywordFilter = keywordFilter == null ? KeywordFilter.ALL : keywordFilter;
    }
}
