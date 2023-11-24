package com.clova.anifriends.domain.recruitment.repository;

import com.clova.anifriends.domain.recruitment.Recruitment;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitmentRepository
    extends JpaRepository<Recruitment, Long>, RecruitmentRepositoryCustom {

    @Query("select r from Recruitment r"
        + " where r.recruitmentId in ("
        + "select a.recruitment.recruitmentId from Applicant a"
        + " where a.volunteer.volunteerId = :volunteerId"
        + " and a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE)")
    Page<Recruitment> findCompletedRecruitments(
        @Param("volunteerId") Long volunteerId,
        Pageable pageable);

    @Query("select r from Recruitment r "
        + "where r.shelter.shelterId = :shelterId "
        + "and r.recruitmentId = :recruitmentId")
    Optional<Recruitment> findByShelterIdAndRecruitmentId(
        @Param("shelterId") long shelterId, @Param("recruitmentId") long recruitmentId);

    @Query("select r from Recruitment r"
        + " join fetch r.images"
        + " where r.recruitmentId = :recruitmentId"
        + " and r.shelter.shelterId = :shelterId")
    Optional<Recruitment> findByShelterIdAndRecruitmentIdWithImages(
        @Param("shelterId") Long shelterId, @Param("recruitmentId") Long recruitmentId);

    @Query("select r from Recruitment r left join fetch r.images where r.recruitmentId = :recruitmentId")
    Optional<Recruitment> findRecruitmentDetail(@Param("recruitmentId") Long recruitmentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Recruitment r where r.recruitmentId = :recruitmentId")
    Optional<Recruitment> findByIdPessimistic(@Param("recruitmentId") Long recruitmentId);
}
