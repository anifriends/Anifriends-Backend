package com.clova.anifriends.domain.applicant.repository;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.NO_SHOW;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.PENDING;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.REFUSED;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicantRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Nested
    @DisplayName("findApprovedByRecruitmentIdAndShelterId 메서드 실행 시")
    class FindApprovedByRecruitmentIdAndShelterId {

        @Test
        @DisplayName("성공: 승인된(출석, 노쇼) 봉사 신청 조회 2명")
        void findApprovedByRecruitmentIdAndShelterId1() {
            // given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicantAttendance = applicant(recruitment, volunteer, ATTENDANCE);
            Applicant applicantNoShow = applicant(recruitment, volunteer, NO_SHOW);
            Applicant applicantPending = applicant(recruitment, volunteer, PENDING);
            Applicant applicantRefused = applicant(recruitment, volunteer, REFUSED);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
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
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicantPending = applicant(recruitment, volunteer, PENDING);
            Applicant applicantRefused = applicant(recruitment, volunteer, REFUSED);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
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
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            recruitmentRepository.save(recruitment);

            Applicant applicantShouldWriteReview = ApplicantFixture.applicantWithStatus(recruitment,
                volunteer, ATTENDANCE);
            Applicant applicantShouldNotWriteReview = ApplicantFixture.applicantWithStatus(
                recruitment,
                volunteer, PENDING);
            Review review = ReviewFixture.review(applicantShouldWriteReview);
            setField(review, "reviewId", 1L);

            applicantRepository.save(applicantShouldWriteReview);
            applicantRepository.save(applicantShouldNotWriteReview);

            // when
            List<Applicant> applyingVolunteers = applicantRepository.findApplyingVolunteers(
                volunteer);

            FindApplyingVolunteersResponse expected = FindApplyingVolunteersResponse.from(
                applyingVolunteers);

            // then
            assertThat(expected.findApplyingVolunteerResponses().get(0).isWritedReview()).isEqualTo(
                true);
            assertThat(expected.findApplyingVolunteerResponses().get(1).isWritedReview()).isEqualTo(
                false);
        }
    }
}
