package com.clova.anifriends.domain.applicant.repository;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    @Query(
        "select s.shelterId as shelterId,"
            + " r.recruitmentId as recruitmentId,"
            + " a.applicantId as applicantId,"
            + " r.title.title as recruitmentTitle,"
            + " s.name.name as shelterName,"
            + " a.status as applicantStatus,"
            + " exists (select rv2 from Review rv2 where rv2.applicant = a) as applicantIsWritedReview,"
            + " r.info.startTime as recruitmentStartTime "
            + "from Applicant a "
            + "join a.recruitment r "
            + "join r.shelter s "
            + "where a.volunteer = :volunteer"
    )
    List<FindApplyingVolunteerResult> findApplyingVolunteers(@Param("volunteer") Volunteer volunteer);

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
        + "join fetch a.volunteer v "
        + "where r.recruitmentId = :recruitmentId "
        + "and s.shelterId = :shelterId "
        + "and (a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE "
        + "or a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.NO_SHOW)")
    List<Applicant> findApprovedApplicants(
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

    @Modifying
    @Query("update Applicant a set a.status = :status "
        + "where a.recruitment.recruitmentId = :recruitmentId "
        + "and a.recruitment.shelter.shelterId = :shelterId "
        + "and a.applicantId in :ids "
        + "and (a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE "
        + "or a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.NO_SHOW)")
    void updateBulkAttendance(
        @Param("shelterId") Long shelterId,
        @Param("recruitmentId") Long recruitmentId,
        @Param("ids") List<Long> ids,
        @Param("status") ApplicantStatus status
    );

    Optional<Applicant> findByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterId(
        Long applicantId, Long recruitmentId, Long shelterId);
}
