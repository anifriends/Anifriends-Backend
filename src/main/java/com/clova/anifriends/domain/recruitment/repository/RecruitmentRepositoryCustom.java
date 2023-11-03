package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentRepositoryCustom {

    Page<Recruitment> findRecruitmentsByShelterOrderByCreatedAt(long shelterId, String keyword,
        LocalDate startDate, LocalDate endDate, Boolean content, Boolean title, Pageable pageable);

    Page<Recruitment> findRecruitmentsByShelterId(long shelterId, Pageable pageable);
}
