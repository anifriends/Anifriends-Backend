package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitmentRepositoryCustom {

    Page<Recruitment> findRecruitments(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, boolean titleContains, boolean contentContains,
        boolean shelterNameContains, Pageable pageable);

    Slice<Recruitment> findRecruitmentsV2(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, boolean titleContains, boolean contentContains,
        boolean shelterNameContains, LocalDateTime createdAt, Long recruitmentId,
        Pageable pageable);

    Long countFindRecruitmentsV2(String keyword, LocalDate startDate,
        LocalDate endDate, Boolean isClosed, boolean titleContains, boolean contentContains,
        boolean shelterNameContains);

    Page<Recruitment> findRecruitmentsByShelterOrderByCreatedAt(long shelterId, String keyword,
        LocalDate startDate, LocalDate endDate, Boolean isClosed, Boolean content, Boolean title,
        Pageable pageable);

    Page<Recruitment> findRecruitmentsByShelterId(long shelterId, Pageable pageable);
}
