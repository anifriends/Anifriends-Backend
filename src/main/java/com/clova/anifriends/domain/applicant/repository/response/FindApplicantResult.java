package com.clova.anifriends.domain.applicant.repository.response;

import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;

public interface FindApplicantResult {

    Long getVolunteerId();
    Long getApplicantId();
    LocalDate getVolunteerBirthDate();
    VolunteerGender getVolunteerGender();
    int getCompletedVolunteerCount();
    int getVolunteerTemperature();
    ApplicantStatus getApplicantStatus();
}
