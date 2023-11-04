package com.clova.anifriends.domain.applicant.support;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import org.springframework.test.util.ReflectionTestUtils;

public class ApplicantFixture {

    public static Applicant applicantWithStatus(Recruitment recruitment, Volunteer volunteer,
        ApplicantStatus status) {
        Applicant applicant = new Applicant(recruitment, volunteer);
        ReflectionTestUtils.setField(applicant, "status", status);
        return applicant;
    }
}
