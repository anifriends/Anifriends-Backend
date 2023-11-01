package com.clova.anifriends.domain.applicant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.domain.applicant.exception.ApplicantBadRequestException;
import com.clova.anifriends.domain.applicant.exception.ApplicantConflictException;
import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.recruitment.wrapper.RecruitmentInfo;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicantTest {

    @Nested
    @DisplayName("applicant 생성 시")
    class NewApplicantTest {

        Shelter shelter = ShelterFixture.shelter();
        RecruitmentInfo recruitmentInfo;
        Recruitment recruitment;
        Volunteer volunteer;
        String status;

        @Test
        @DisplayName("성공")
        void newApplicant() {
            //given
            recruitmentInfo = new RecruitmentInfo(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(7),
                false,
                30
            );
            recruitment = RecruitmentFixture.recruitment(shelter);
            setField(recruitment, "info", recruitmentInfo);
            volunteer = VolunteerFixture.volunteer();
            status = ApplicantStatus.PENDING.getValue();

            //when
            Applicant applicant = new Applicant(recruitment, volunteer, status);

            //then
            assertThat(applicant.getStatus()).isEqualTo(ApplicantStatus.valueOf(status));
        }

        @Test
        @DisplayName("예외: 모집이 마감된 경우")
        void throwExceptionWhenRecruitmentIsClosed() {
            //given
            recruitmentInfo = new RecruitmentInfo(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(2),
                true,
                30
            );
            recruitment = RecruitmentFixture.recruitment(shelter);
            setField(recruitment, "info", recruitmentInfo);
            volunteer = VolunteerFixture.volunteer();
            status = ApplicantStatus.PENDING.getValue();

            //when
            Exception exception = catchException(() -> new Applicant(recruitment, volunteer, status));

            //then
            assertThat(exception).isInstanceOf(ApplicantBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 모집 마감 시간이 지난 경우")
        void throwExceptionWhenRecruitmentDeadLineIsOver() {
            //given
            recruitmentInfo = new RecruitmentInfo(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                false,
                30
            );
            recruitment = RecruitmentFixture.recruitment(shelter);
            setField(recruitment, "info", recruitmentInfo);
            volunteer = VolunteerFixture.volunteer();
            status = ApplicantStatus.PENDING.getValue();

            //when
            Exception exception = catchException(() -> new Applicant(recruitment, volunteer, status));

            //then
            assertThat(exception).isInstanceOf(ApplicantBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 봉사 모집이 null인 경우")
        void throwExceptionWhenRecruitmentIsNull() {
            //given
            volunteer = VolunteerFixture.volunteer();
            status = ApplicantStatus.PENDING.getValue();

            //when
            Exception exception = catchException(() -> new Applicant(recruitment, volunteer, status));

            //then
            assertThat(exception).isInstanceOf(ApplicantBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 봉사자가 null인 경우")
        void throwExceptionWhenVolunteerIsNull() {
            //given
            recruitmentInfo = new RecruitmentInfo(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(7),
                false,
                30
            );
            status = ApplicantStatus.PENDING.getValue();

            //when
            Exception exception = catchException(() -> new Applicant(recruitment, volunteer, status));

            //then
            assertThat(exception).isInstanceOf(ApplicantBadRequestException.class);
        }

        @Test
        @DisplayName("예외: 봉사 모집 인원이 마감된 경우")
        void throwExceptionWhenRecruitmentCapacityIsOver() {
            //given
            recruitmentInfo = new RecruitmentInfo(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(7),
                false,
                0
            );
            recruitment = RecruitmentFixture.recruitment(shelter);
            setField(recruitment, "info", recruitmentInfo);
            volunteer = VolunteerFixture.volunteer();
            status = ApplicantStatus.PENDING.getValue();

            //when
            Exception exception = catchException(() -> new Applicant(recruitment, volunteer, status));

            //then
            assertThat(exception).isInstanceOf(ApplicantConflictException.class);
        }
    }
}
