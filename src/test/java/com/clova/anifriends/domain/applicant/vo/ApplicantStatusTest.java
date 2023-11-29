package com.clova.anifriends.domain.applicant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicantStatusTest {


    @Nested
    @DisplayName("출석 상태 수정 시")
    class ConvertToApproved {

        @Test
        @DisplayName("성공: 현재 시간이 시작 시간 이후이고 출석 상태가 ATTENDANCE 인 경우")
        void convertToApproved1() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Volunteer volunteer = VolunteerFixture.volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer,
                ApplicantStatus.ATTENDANCE);

            // when
            ApplicantStatus afterApplicantStatus = applicant.getStatus()
                .convertToApproved(LocalDateTime.now().minusHours(2));

            // then
            assertThat(afterApplicantStatus).isEqualTo(ApplicantStatus.ATTENDANCE);
        }

        @Test
        @DisplayName("성공: 현재 시간이 시작 시간 이후이고 출석 상태가 NOSHOW 인 경우")
        void convertToApproved2() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Volunteer volunteer = VolunteerFixture.volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer,
                ApplicantStatus.NOSHOW);

            // when
            ApplicantStatus afterApplicantStatus = applicant.getStatus()
                .convertToApproved(LocalDateTime.now().minusHours(2));

            // then
            assertThat(afterApplicantStatus).isEqualTo(ApplicantStatus.NOSHOW);
        }

        @Test
        @DisplayName("성공: 현재 시간이 시작 시간 이전이고 출석 상태가 ATTENDANCE 인 경우")
        void convertToApproved3() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Volunteer volunteer = VolunteerFixture.volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer,
                ApplicantStatus.ATTENDANCE);

            // when
            ApplicantStatus afterApplicantStatus = applicant.getStatus()
                .convertToApproved(LocalDateTime.now().plusHours(2));

            // then
            assertThat(afterApplicantStatus).isEqualTo(ApplicantStatus.APPROVED);
        }
    }
}
