package com.clova.anifriends.domain.recruitment.dto.request;

import java.time.LocalDate;

public record FindRecruitmentsByShelterRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    Boolean content,
    Boolean title,
    Integer pageSize,
    Integer pageNumber
) {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    public FindRecruitmentsByShelterRequest(String keyword, LocalDate startDate, LocalDate endDate, Boolean content, Boolean title, Integer pageSize, Integer pageNumber) {
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content == null || content;
        this.title = title == null || title;
        this.pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        this.pageNumber = pageNumber == null ? DEFAULT_PAGE_NUMBER : pageNumber;
    }
}
