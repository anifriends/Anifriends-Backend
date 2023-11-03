package com.clova.anifriends.domain.applicant.repository;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    boolean existsByRecruitmentAndVolunteer(Recruitment recruitment, Volunteer volunteer);

    @Query("select a from Applicant a "
        + "join fetch a.recruitment r "
        + "join fetch r.shelter s "
        + "where r.recruitmentId = :recruitmentId "
        + "and s.shelterId = :shelterId "
        + "and (a.status = 'ATTENDANCE' or a.status = 'NO_SHOW')")
    List<Applicant> findApprovedByRecruitmentIdAndShelterId(
        @Param("recruitmentId") Long recruitmentId,
        @Param("shelterId") Long shelterId
    );
}
