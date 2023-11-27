package com.clova.anifriends.domain.volunteer.repository;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.vo.VolunteerEmail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmail(VolunteerEmail email);

    boolean existsByEmail(VolunteerEmail email);

    @Query("select v from Volunteer v "
        + "join fetch v.applicants a "
        + "join fetch a.recruitment r "
        + "where r.shelter.shelterId = :shelterId "
        + "and r.recruitmentId = :recruitmentId "
        + "and a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE "
        + "and a.applicantId in :noShowIds")
    List<Volunteer> findAttendedByNoShowIds(
        @Param("shelterId") Long shelterId,
        @Param("recruitmentId") Long recruitmentId,
        @Param("noShowIds") List<Long> noShowIds);

    @Query("select v from Volunteer v "
        + "join fetch v.applicants a "
        + "join fetch a.recruitment r "
        + "where r.shelter.shelterId = :shelterId "
        + "and r.recruitmentId = :recruitmentId "
        + "and a.status = com.clova.anifriends.domain.applicant.vo.ApplicantStatus.NO_SHOW "
        + "and a.applicantId in :attendedIds")
    List<Volunteer> findNoShowByAttendedIds(
        @Param("shelterId") Long shelterId,
        @Param("recruitmentId") Long recruitmentId,
        @Param("attendedIds") List<Long> attendedIds);
}
