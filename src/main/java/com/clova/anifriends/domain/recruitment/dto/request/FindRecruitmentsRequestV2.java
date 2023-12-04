package com.clova.anifriends.domain.recruitment.dto.request;

import com.clova.anifriends.domain.recruitment.controller.KeywordFilter;
import com.clova.anifriends.domain.recruitment.controller.RecruitmentStatusFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FindRecruitmentsRequestV2(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    RecruitmentStatusFilter closedFilter,
    KeywordFilter keywordFilter,
    Long recruitmentId,
    LocalDateTime createdAt
) {

    public FindRecruitmentsRequestV2(
        String keyword,
        LocalDate startDate,
        LocalDate endDate,
        RecruitmentStatusFilter closedFilter,
        KeywordFilter keywordFilter,
        Long recruitmentId,
        LocalDateTime createdAt
    ) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closedFilter = closedFilter == null ? RecruitmentStatusFilter.ALL : closedFilter;
        this.keywordFilter = keywordFilter == null ? KeywordFilter.ALL : keywordFilter;
        this.recruitmentId = recruitmentId;
        this.createdAt = createdAt;
    }
}
