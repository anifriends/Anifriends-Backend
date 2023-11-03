package com.clova.anifriends.domain.recruitment.dto.request;

import java.time.LocalDate;

public record FindRecruitmentsByShelterRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    Boolean content,
    Boolean title
) {

    public FindRecruitmentsByShelterRequest(String keyword, LocalDate startDate, LocalDate endDate, Boolean content, Boolean title) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content == null || content;
        this.title = title == null || title;
    }
}
