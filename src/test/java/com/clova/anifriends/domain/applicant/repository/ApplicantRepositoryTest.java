package com.clova.anifriends.domain.applicant.repository;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.NO_SHOW;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.PENDING;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.REFUSED;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class ApplicantRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findApprovedByRecruitmentIdAndShelterId 메서드 실행 시")
    class FindApprovedByRecruitmentIdAndShelterId {

        @Test
        @DisplayName("성공: 승인된(출석, 노쇼) 봉사 신청 조회 2명")
        void findApprovedByRecruitmentIdAndShelterId1() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteerAttendance = volunteer();
            Volunteer volunteerNoShow = volunteer();
            Volunteer volunteerPending = volunteer();
            Volunteer volunteerRefused = volunteer();

            Recruitment recruitment = recruitment(shelter);
            Applicant applicantAttendance = applicant(recruitment, volunteerAttendance, ATTENDANCE);
            Applicant applicantNoShow = applicant(recruitment, volunteerNoShow, NO_SHOW);
            Applicant applicantPending = applicant(recruitment, volunteerPending, PENDING);
            Applicant applicantRefused = applicant(recruitment, volunteerRefused, REFUSED);

            shelterRepository.save(shelter);
            volunteerRepository.saveAll(
                List.of(volunteerAttendance, volunteerNoShow, volunteerPending, volunteerRefused)
            );
            recruitmentRepository.save(recruitment);
            applicantRepository.saveAll(
                List.of(applicantAttendance, applicantNoShow, applicantPending, applicantRefused)
            );
            List<Applicant> expected = List.of(applicantAttendance, applicantNoShow);

            // when
            List<Applicant> result = applicantRepository
                .findApprovedByRecruitmentIdAndShelterId(recruitment.getRecruitmentId(),
                    shelter.getShelterId());

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("성공: 승인된(출석, 노쇼) 봉사 신청 조회 0 명")
        void findApprovedByRecruitmentIdAndShelterId2() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteerPending = volunteer();
            Volunteer volunteerRefused = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicantPending = applicant(recruitment, volunteerPending, PENDING);
            Applicant applicantRefused = applicant(recruitment, volunteerRefused, REFUSED);

            shelterRepository.save(shelter);
            volunteerRepository.saveAll(
                List.of(volunteerPending, volunteerRefused)
            );
            recruitmentRepository.save(recruitment);
            applicantRepository.saveAll(
                List.of(applicantPending, applicantRefused)
            );
            List<Applicant> expected = List.of();

            // when
            List<Applicant> result = applicantRepository
                .findApprovedByRecruitmentIdAndShelterId(recruitment.getRecruitmentId(),
                    shelter.getShelterId());

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findApplyingVolunteers 실행 시")
    class FindApplyingVolunteersTest {

        @Test
        @DisplayName("성공")
        void findApplyingVolunteers() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();

            Recruitment recruitment1 = RecruitmentFixture.recruitment(shelter);
            Recruitment recruitment2 = RecruitmentFixture.recruitment(shelter);
            Recruitment recruitment3 = RecruitmentFixture.recruitment(shelter);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.saveAll(List.of(recruitment1, recruitment2, recruitment3));

            Applicant applicantShouldWriteReview = ApplicantFixture.applicant(
                recruitment1, volunteer, ATTENDANCE);
            Applicant applicantShouldNotWriteReview1 = ApplicantFixture.applicant(
                recruitment2, volunteer, PENDING);
            Applicant applicantShouldNotWriteReview2 = ApplicantFixture.applicant(
                recruitment3, volunteer, ATTENDANCE);
            Review review = ReviewFixture.review(applicantShouldNotWriteReview2);
            setField(review, "reviewId", 1L);

            applicantRepository.save(applicantShouldWriteReview);
            applicantRepository.save(applicantShouldNotWriteReview1);
            applicantRepository.save(applicantShouldNotWriteReview2);

            // when
            List<Applicant> applyingVolunteers = applicantRepository.findApplyingVolunteers(
                volunteer);

            FindApplyingVolunteersResponse expected = FindApplyingVolunteersResponse.from(
                applyingVolunteers);

            // then
            assertThat(expected.findApplyingVolunteerResponses().get(0)
                .applicantIsWritedReview()).isTrue();
            assertThat(expected.findApplyingVolunteerResponses().get(1)
                .applicantIsWritedReview()).isFalse();
            assertThat(expected.findApplyingVolunteerResponses().get(2)
                .applicantIsWritedReview()).isFalse();
        }
    }

    @Nested
    @DisplayName("findByRecruitmentIdAndShelterId")
    class FindRecruitmentIdAndShelterIdTest {

        @Test
        @DisplayName("성공")
        void findByRecruitmentIdAndShelterId() {
            // given
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);

            Volunteer volunteerAttendance = volunteer();
            Volunteer volunteerNoShow = volunteer();
            Volunteer volunteerPending = volunteer();
            Volunteer volunteerRefused = volunteer();

            Applicant applicantAttendance = applicant(recruitment, volunteerAttendance, ATTENDANCE);
            Applicant applicantNoShow = applicant(recruitment, volunteerNoShow, NO_SHOW);
            Applicant applicantPending = applicant(recruitment, volunteerPending, PENDING);
            Applicant applicantRefused = applicant(recruitment, volunteerRefused, REFUSED);

            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);
            volunteerRepository.saveAll(List.of(
                volunteerAttendance,
                volunteerNoShow,
                volunteerPending,
                volunteerRefused
            ));
            applicantRepository.saveAll(
                List.of(applicantAttendance, applicantNoShow, applicantPending, applicantRefused)
            );
            List<Applicant> expected = List.of(applicantAttendance, applicantNoShow,
                applicantPending, applicantRefused);

            // when
            List<Applicant> result = applicantRepository
                .findByRecruitmentIdAndShelterId(recruitment.getRecruitmentId(),
                    shelter.getShelterId());

            // then
            assertThat(result).isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("updateAttendance 실행 시")
    class UpdateAttendanceTest {

        @Test
        @DisplayName("성공")
        void updateAttendance() {
            // given
            Shelter shelter = shelter();

            Volunteer volunteerAttendanceToNoShow = volunteer();
            Volunteer volunteerNoShowToAttendance = volunteer();
            Volunteer volunteerPending = volunteer();
            Volunteer volunteerRefuse = volunteer();

            Recruitment recruitment = recruitment(shelter);
            Applicant applicantAttendanceToNoShow = applicant(recruitment,
                volunteerAttendanceToNoShow, ATTENDANCE);
            Applicant applicantNoShowToAttendance = applicant(recruitment,
                volunteerNoShowToAttendance, NO_SHOW);
            Applicant applicantPending = applicant(recruitment, volunteerPending, PENDING);
            Applicant applicantRefused = applicant(recruitment, volunteerRefuse, REFUSED);

            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);
            volunteerRepository.saveAll(List.of(
                volunteerAttendanceToNoShow,
                volunteerNoShowToAttendance,
                volunteerPending,
                volunteerRefuse));
            applicantRepository.saveAll(List.of(
                applicantAttendanceToNoShow,
                applicantNoShowToAttendance,
                applicantPending,
                applicantRefused));

            List<Long> attendedIds = List.of(applicantNoShowToAttendance.getApplicantId());
            List<Long> noShowIds = List.of(applicantAttendanceToNoShow.getApplicantId());

            // when
            applicantRepository.updateBulkAttendance(shelter.getShelterId(),
                recruitment.getRecruitmentId(), attendedIds, ATTENDANCE);
            applicantRepository.updateBulkAttendance(shelter.getShelterId(),
                recruitment.getRecruitmentId(), noShowIds, NO_SHOW);

            // then
            Optional<Applicant> persistedApplicantNoShowToAttendance = applicantRepository.findById(
                applicantNoShowToAttendance.getApplicantId());
            assertThat(persistedApplicantNoShowToAttendance).isNotEmpty();
            assertThat(persistedApplicantNoShowToAttendance.get().getStatus()).isEqualTo(
                ATTENDANCE);

            Optional<Applicant> persistedApplicantAttendanceToNoShow = applicantRepository.findById(
                applicantAttendanceToNoShow.getApplicantId());
            assertThat(persistedApplicantAttendanceToNoShow).isNotEmpty();
            assertThat(persistedApplicantAttendanceToNoShow.get().getStatus()).isEqualTo(NO_SHOW);

            Optional<Applicant> persistedApplicantPending = applicantRepository.findById(
                applicantPending.getApplicantId());
            assertThat(persistedApplicantPending).isNotEmpty();
            assertThat(persistedApplicantPending.get().getStatus()).isEqualTo(PENDING);

            Optional<Applicant> persistedApplicantRefused = applicantRepository.findById(
                applicantRefused.getApplicantId());
            assertThat(persistedApplicantRefused).isNotEmpty();
            assertThat(persistedApplicantRefused.get().getStatus()).isEqualTo(REFUSED);
        }
    }

    @Nested
    @DisplayName("findByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterId 실행 시")
    class FindByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterIdTest {

        @Test
        @DisplayName("성공")
        void findByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterId() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.save(applicant);

            // when
            Optional<Applicant> expected = applicantRepository
                .findByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterId(
                    applicant.getApplicantId(),
                    recruitment.getRecruitmentId(),
                    shelter.getShelterId()
                );

            // then
            assertThat(expected).isNotEmpty();
            assertThat(expected.get()).isEqualTo(applicant);
        }
    }

    @Nested
    @DisplayName("save 메서드 실행 시")
    class SaveTest {

        @Test
        @DisplayName("예외(DataIntegrityViolationException): 중복된 봉사신청")
        void exceptionWhenDuplicateApply() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            Applicant duplicateApplicant = applicant(recruitment, volunteer, ATTENDANCE);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);
            applicantRepository.save(applicant);

            // when
            Exception exception = catchException(
                () -> applicantRepository.save(duplicateApplicant));

            // then
            assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);

        }
    }
}
