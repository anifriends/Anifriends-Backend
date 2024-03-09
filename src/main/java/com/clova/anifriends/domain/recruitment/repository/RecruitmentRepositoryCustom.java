package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.service.KeywordCondition;
import com.clova.anifriends.domain.recruitment.service.KeywordConditionByShelter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitmentRepositoryCustom {

    Page<Recruitment> findRecruitments(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition, Pageable pageable);

    Slice<Recruitment> findRecruitmentsV2(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition,
        LocalDateTime createdAt, Long recruitmentId, Pageable pageable);

    Long countFindRecruitmentsV2(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, KeywordCondition keywordCondition);

    Page<Recruitment> findRecruitmentsByShelterOrderByCreatedAt(long shelterId, String keyword,
        LocalDate startDate, LocalDate endDate, Boolean isClosed,
        KeywordConditionByShelter keywordConditionByShelter, Pageable pageable);

    Page<Recruitment> findShelterRecruitments(long shelterId, Pageable pageable);
}
