package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentRepositoryCustom {

    Page<Recruitment> findRecruitments(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, boolean titleFilter, boolean contentFilter,
        boolean shelterNameFilter, Pageable pageable);
}
