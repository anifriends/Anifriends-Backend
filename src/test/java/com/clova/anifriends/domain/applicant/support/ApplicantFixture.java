package com.clova.anifriends.domain.applicant.support;

import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.volunteer.Volunteer;
import org.springframework.test.util.ReflectionTestUtils;

public class ApplicantFixture {

    public static Applicant applicant(Recruitment recruitment, Volunteer volunteer,
        ApplicantStatus status) {
        Applicant applicant = new Applicant(recruitment, volunteer);
        ReflectionTestUtils.setField(applicant, "status", status);
        return applicant;
    }

    public static Applicant applicant(Recruitment recruitment, Volunteer volunteer) {
        return applicant(recruitment, volunteer, ApplicantStatus.PENDING);

    }

    public static Applicant applicant(Recruitment recruitment, Volunteer volunteer,
        ApplicantStatus status, Long applicantId) {
        Applicant applicant = applicant(recruitment, volunteer, status);
        ReflectionTestUtils.setField(applicant, "applicantId", applicantId);
        return applicant;
    }

    public static Applicant applicantWithReview(Recruitment recruitment, Volunteer volunteer) {
        Applicant applicant = applicant(recruitment, volunteer);
        ReflectionTestUtils.setField(applicant, "status", ATTENDANCE);
        ReflectionTestUtils.setField(applicant, "review", review(applicant));
        return applicant;
    }
}
