package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface RecruitmentRepositoryCustom {

    List<Recruitment> findRecruitmentsByShelterOrderByCreatedAt(long shelterId, String keyword,
        LocalDate startDate, LocalDate endDate, boolean content, boolean title, Pageable pageable);

}
