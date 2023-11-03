package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>,
    RecruitmentRepositoryCustom {

    @Query("select r from Recruitment r where r.shelter.shelterId = :shelterId and r.recruitmentId = :recruitmentId")
    Optional<Recruitment> findByShelterIdAndRecruitmentId(@Param("shelterId") Long shelterId,
        @Param("recruitmentId") Long recruitmentId);
}
