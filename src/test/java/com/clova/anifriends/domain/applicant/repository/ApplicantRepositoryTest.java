package com.clova.anifriends.domain.applicant.repository;

import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplyingVolunteersResponse;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicantRepositoryTest extends BaseRepositoryTest {

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

            Applicant applicantShouldWriteReview = ApplicantFixture.applicant(recruitment,
                volunteer, ATTENDANCE);
            Applicant applicantShouldNotWriteReview = ApplicantFixture.applicant(recruitment,
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
