package com.clova.anifriends.domain.recruitment.dto.request;

import com.clova.anifriends.domain.recruitment.controller.RecruitmentStatusFilter;
import java.time.LocalDate;

public record FindRecruitmentsRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    RecruitmentStatusFilter closedFilter,
    Boolean title,
    Boolean content,
    Boolean shelterName
) {

    public FindRecruitmentsRequest(String keyword, LocalDate startDate,
        LocalDate endDate,
        RecruitmentStatusFilter closedFilter, Boolean title, Boolean content, Boolean shelterName) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.closedFilter = closedFilter == null ? RecruitmentStatusFilter.ALL : closedFilter;
        this.title = title == null || title;
        this.content = content == null || content;
        this.shelterName = shelterName == null || shelterName;
    }
}
