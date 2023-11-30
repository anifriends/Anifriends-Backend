package com.clova.anifriends.domain.applicant.repository;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.NOSHOW;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.PENDING;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.REFUSED;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.response.FindApplicantResult;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
            Applicant applicantNoShow = applicant(recruitment, volunteerNoShow, NOSHOW);
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
                .findApprovedApplicants(recruitment.getRecruitmentId(),
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
                .findApprovedApplicants(recruitment.getRecruitmentId(),
                    shelter.getShelterId());

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findApplyingVolunteers 실행 시")
    class FindApplyingVolunteersV2Test {

        Volunteer volunteer;
        Shelter shelter;
        Recruitment recruitment1;
        Recruitment recruitment2;
        Recruitment recruitment3;

        @BeforeEach
        void setUp() {
            volunteer = VolunteerFixture.volunteer();
            shelter = ShelterFixture.shelter();
            recruitment1 = RecruitmentFixture.recruitment(shelter);
            recruitment2 = RecruitmentFixture.recruitment(shelter);
            recruitment3 = RecruitmentFixture.recruitment(shelter);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.saveAll(List.of(recruitment1, recruitment2, recruitment3));
        }

        @Test
        @DisplayName("성공: 봉사 신청 3개, 리뷰 작성 1개")
        void findApplyingVolunteersWhen3Recruitment1Review() {
            // given
            Applicant applicantShouldWriteReview = ApplicantFixture.applicant(
                recruitment1, volunteer, ATTENDANCE);
            Applicant applicantShouldNotWriteReview1 = ApplicantFixture.applicant(
                recruitment2, volunteer, PENDING);
            Applicant applicantShouldNotWriteReview2 = ApplicantFixture.applicant(
                recruitment3, volunteer, ATTENDANCE);
            Review review = ReviewFixture.review(applicantShouldWriteReview);

            applicantRepository.save(applicantShouldWriteReview);
            applicantRepository.save(applicantShouldNotWriteReview1);
            applicantRepository.save(applicantShouldNotWriteReview2);
            reviewRepository.save(review);

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<FindApplyingVolunteerResult> applyingVolunteers
                = applicantRepository.findApplyingVolunteers(volunteer, pageRequest);

            // then
            List<Long> reviewExists = applyingVolunteers.stream()
                .filter(FindApplyingVolunteerResult::getApplicantIsWritedReview)
                .map(FindApplyingVolunteerResult::getApplicantId)
                .toList();
            assertThat(reviewExists).containsExactly(applicantShouldWriteReview.getApplicantId());
        }

        @Test
        @DisplayName("성공: 봉사 신청 3개, 리뷰 작성 2개")
        void findApplyingVolunteersWhen3Recruitment2Review() {
            // given
            Applicant applicantShouldWriteReview1 = ApplicantFixture.applicant(
                recruitment1, volunteer, ATTENDANCE);
            Applicant applicantShouldWriteReview2 = ApplicantFixture.applicant(
                recruitment2, volunteer, ATTENDANCE);
            Applicant applicantShouldNotWriteReview = ApplicantFixture.applicant(
                recruitment3, volunteer, ATTENDANCE);
            Review review1 = ReviewFixture.review(applicantShouldWriteReview1);
            Review review2 = ReviewFixture.review(applicantShouldWriteReview2);

            applicantRepository.save(applicantShouldWriteReview1);
            applicantRepository.save(applicantShouldWriteReview2);
            applicantRepository.save(applicantShouldNotWriteReview);
            reviewRepository.save(review1);
            reviewRepository.save(review2);

            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<FindApplyingVolunteerResult> applyingVolunteers
                = applicantRepository.findApplyingVolunteers(volunteer, pageRequest);

            // then
            List<Long> reviewExists = applyingVolunteers.stream()
                .filter(FindApplyingVolunteerResult::getApplicantIsWritedReview)
                .map(FindApplyingVolunteerResult::getApplicantId)
                .toList();
            assertThat(reviewExists)
                .containsExactly(
                    applicantShouldWriteReview1.getApplicantId(),
                    applicantShouldWriteReview2.getApplicantId()
                    );
        }
    }

    @Nested
    @DisplayName("findApplicants 메서드 실행 시")
    class FindApplicantsTest {

        Shelter shelter;
        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);
        }

        @Test
        @DisplayName("성공")
        void findApplicants() {
            //given
            Volunteer volunteerAttendance = volunteer();
            Volunteer volunteerNoShow = volunteer();
            Applicant applicantAttendance = applicant(recruitment, volunteerAttendance, ATTENDANCE);
            Applicant applicantNoShow = applicant(recruitment, volunteerNoShow, NOSHOW);

            volunteerRepository.saveAll(
                List.of(volunteerAttendance, volunteerNoShow));
            applicantRepository.saveAll(
                List.of(applicantAttendance, applicantNoShow));

            //when
            List<FindApplicantResult> applicants = applicantRepository.findApplicants(
                recruitment, shelter);

            //then
            assertThat(applicants).hasSize(2);
            FindApplicantResult result0 = applicants.get(0);
            assertThat(result0.getVolunteerId()).isEqualTo(volunteerAttendance.getVolunteerId());
            assertThat(result0.getApplicantId()).isEqualTo(applicantAttendance.getApplicantId());
            assertThat(result0.getVolunteerBirthDate()).isEqualTo(volunteerAttendance.getBirthDate());
            assertThat(result0.getVolunteerGender()).isEqualTo(volunteerAttendance.getGender());
            assertThat(result0.getCompletedVolunteerCount()).isEqualTo(1);
            assertThat(result0.getVolunteerTemperature())
                .isEqualTo(volunteerAttendance.getTemperature());
            assertThat(result0.getApplicantStatus())
                .isEqualTo(applicantAttendance.getStatus());

            FindApplicantResult result1 = applicants.get(1);
            assertThat(result1.getApplicantStatus()).isEqualTo(applicantNoShow.getStatus());
            assertThat(result1.getCompletedVolunteerCount()).isZero();
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
                volunteerNoShowToAttendance, NOSHOW);
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
                recruitment.getRecruitmentId(), noShowIds, NOSHOW);

            entityManager.flush();
            entityManager.clear();

            // then
            Optional<Applicant> persistedApplicantNoShowToAttendance = applicantRepository.findById(
                applicantNoShowToAttendance.getApplicantId());
            assertThat(persistedApplicantNoShowToAttendance).isNotEmpty();
            assertThat(persistedApplicantNoShowToAttendance.get().getStatus()).isEqualTo(
                ATTENDANCE);

            Optional<Applicant> persistedApplicantAttendanceToNoShow = applicantRepository.findById(
                applicantAttendanceToNoShow.getApplicantId());
            assertThat(persistedApplicantAttendanceToNoShow).isNotEmpty();
            assertThat(persistedApplicantAttendanceToNoShow.get().getStatus()).isEqualTo(NOSHOW);

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
