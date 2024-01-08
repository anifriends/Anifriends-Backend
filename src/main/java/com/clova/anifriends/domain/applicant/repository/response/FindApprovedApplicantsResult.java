package com.clova.anifriends.domain.applicant.repository.response;

import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;

public interface FindApprovedApplicantsResult {

    Long getVolunteerId();

    Long getApplicantId();

    String getVolunteerName();

    LocalDate getVolunteerBirthDate();

    VolunteerGender getVolunteerGender();

    String getVolunteerPhoneNumber();

    ApplicantStatus getApplicantStatus();
}
