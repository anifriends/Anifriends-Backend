package com.clova.anifriends.domain.applicant.support;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import org.springframework.test.util.ReflectionTestUtils;

public class ApplicantFixture {

    public static Applicant applicantWithPending(Recruitment recruitment, Volunteer volunteer) {
        Applicant applicant = new Applicant(recruitment, volunteer);
        ReflectionTestUtils.setField(applicant, "status", ApplicantStatus.PENDING);
        return applicant;
    }

    public static Applicant applicantWithAttendance(Recruitment recruitment, Volunteer volunteer) {
        Applicant applicant = new Applicant(recruitment, volunteer);
        ReflectionTestUtils.setField(applicant, "status", ApplicantStatus.ATTENDANCE);
        return applicant;
    }
}
