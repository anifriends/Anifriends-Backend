package com.clova.anifriends.domain.recruitment.dto.request;

import java.time.LocalDate;

public record FindRecruitmentsRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    String isClosed,
    Boolean title,
    Boolean content,
    Boolean shelterName
) {

    public FindRecruitmentsRequest(String keyword, LocalDate startDate,
        LocalDate endDate,
        String isClosed, Boolean title, Boolean content, Boolean shelterName) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isClosed = isClosed == null ? "ALL" : isClosed;
        this.title = title == null || title;
        this.content = content == null || content;
        this.shelterName = shelterName == null || shelterName;
    }
}
