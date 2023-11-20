package com.clova.anifriends.domain.applicant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.exception.ApplicantConflictException;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ApplicantIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ApplicantService applicantService;

    @Nested
    @DisplayName("registerApplicant 메서드 호출 시")
    class RegisterApplicantTest {

        @Test
        @DisplayName("예외(ApplicantConflictException): 중복 봉사 신청")
        void exceptionWhenDuplicateApply() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.save(applicant);

            // when
            Exception exception = catchException(
                () -> applicantService.registerApplicant(recruitment.getRecruitmentId(),
                    volunteer.getVolunteerId()));

            // then
            assertThat(exception).isInstanceOf(ApplicantConflictException.class);
        }
    }

}
