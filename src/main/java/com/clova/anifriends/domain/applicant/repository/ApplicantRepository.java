package com.clova.anifriends.domain.applicant.repository;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    boolean existsByRecruitmentAndVolunteer(Recruitment recruitment, Volunteer volunteer);

    @Query(
        "select a "
            + "from Applicant a "
            + "left join fetch a.recruitment "
            + "left join fetch a.volunteer "
            + "left join fetch a.review "
            + "where a.volunteer = :volunteer"
    )
    List<Applicant> findApplyingVolunteers(
        @Param("volunteer") Volunteer volunteer);

    @Query("select a from Applicant a "
        + "where a.applicantId = :applicantId "
        + "and a.volunteer.volunteerId = :volunteerId")
    Optional<Applicant> findByApplicantIdAndVolunteerId(
        @Param("applicantId") Long applicantId,
        @Param("volunteerId") Long volunteerId
    );

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

    @Query("select a from Applicant a "
        + "join fetch a.recruitment r "
        + "join fetch r.shelter s "
        + "where r.recruitmentId = :recruitmentId "
        + "and s.shelterId = :shelterId ")
    List<Applicant> findByRecruitmentIdAndShelterId(
        @Param("recruitmentId") Long recruitmentId,
        @Param("shelterId") Long shelterId
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Applicant a set a.status = "
        + "case when a.applicantId in :attendedIds "
        + "then 'ATTENDANCE' else 'NO_SHOW' end "
        + "where a.recruitment.recruitmentId = :recruitmentId "
        + "and a.recruitment.shelter.shelterId = :shelterId "
        + "and (a.status = 'ATTENDANCE' or a.status = 'NO_SHOW')")
    void updateBulkAttendance(
        @Param("shelterId") Long shelterId,
        @Param("recruitmentId") Long recruitmentId,
        @Param("attendedIds") List<Long> attendedIds
    );
}
