package com.clova.anifriends.domain.recruitment.dto.request;

import com.clova.anifriends.domain.recruitment.controller.RecruitmentStatusFilter;
import java.time.LocalDate;

public record FindRecruitmentsByShelterRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    RecruitmentStatusFilter closedFilter,
    Boolean content,
    Boolean title
) {

    public FindRecruitmentsByShelterRequest(String keyword, LocalDate startDate, LocalDate endDate,
        RecruitmentStatusFilter closedFilter, Boolean content, Boolean title) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closedFilter = closedFilter == null ? RecruitmentStatusFilter.ALL : closedFilter;
        this.content = content == null || content;
        this.title = title == null || title;
    }
}
