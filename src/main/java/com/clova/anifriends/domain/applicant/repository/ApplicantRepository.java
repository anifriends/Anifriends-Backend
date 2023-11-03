package com.clova.anifriends.domain.applicant.repository;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    boolean existsByRecruitmentAndVolunteer(Recruitment recruitment, Volunteer volunteer);

    @Query("select a from Applicant a "
        + "where a.applicantId = :applicantId "
        + "and a.volunteer.volunteerId = :volunteerId")
    Optional<Applicant> findByApplicantIdAndVolunteerId(
        @Param("applicantId") Long applicantId,
        @Param("volunteerId") Long volunteerId
    );
}
