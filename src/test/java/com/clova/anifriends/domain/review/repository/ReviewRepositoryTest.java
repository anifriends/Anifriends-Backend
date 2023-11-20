package com.clova.anifriends.domain.review.repository;


import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class ReviewRepositoryTest extends BaseRepositoryTest {

    @Test
    @DisplayName("성공")
    void findByIdAndVolunteerId() {
        //given
        Shelter shelter = shelter();
        Volunteer volunteer = volunteer();
        Recruitment recruitment = recruitment(shelter);
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        Review review = review(applicant);

        shelterRepository.save(shelter);
        volunteerRepository.save(volunteer);
        recruitmentRepository.save(recruitment);
        applicantRepository.save(applicant);
        reviewRepository.save(review);

        //when
        Optional<Review> persistedReview = reviewRepository.findByReviewIdAndVolunteerId(
            review.getReviewId(), volunteer.getVolunteerId());

        //then
        assertThat(persistedReview).isEqualTo(Optional.of(review));
    }

    @Test
    @DisplayName("성공")
    void findAllByVolunteerVolunteerIdOrderByCreatedAtDesc() {
        //given
        Shelter shelter = shelter();
        Volunteer volunteer1 = volunteer();
        setField(volunteer1, "volunteerId", 1L);
        Volunteer volunteer2 = volunteer();
        setField(volunteer2, "volunteerId", 2L);
        Recruitment recruitment = recruitment(shelter);
        Applicant applicant1 = applicant(recruitment, volunteer1, ATTENDANCE);
        Applicant applicant2 = applicant(recruitment, volunteer2, ATTENDANCE);
        Review review1 = review(applicant1);
        Review review2 = review(applicant2);

        shelterRepository.save(shelter);
        volunteerRepository.save(volunteer1);
        volunteerRepository.save(volunteer2);
        recruitmentRepository.save(recruitment);
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        applicantRepository.save(applicant1);
        applicantRepository.save(applicant2);

        //when
        List<Review> persistedReview = reviewRepository.findAllByVolunteerVolunteerIdOrderByCreatedAtDesc(
            volunteer1.getVolunteerId(), null).getContent();

        //then
        assertThat(persistedReview).isEqualTo(List.of(review1));
    }

    @Test
    @DisplayName("예외(DataIntegrityViolationException): 중복된 리뷰")
    void exceptionWhenDuplicateReview() {
        // given
        Shelter shelter = shelter();
        Volunteer volunteer = volunteer();
        Recruitment recruitment = recruitment(shelter);
        Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
        Review review = review(applicant);
        Review duplicateReview = review(applicant);

        shelterRepository.save(shelter);
        volunteerRepository.save(volunteer);
        recruitmentRepository.save(recruitment);
        applicantRepository.save(applicant);
        reviewRepository.save(review);

        // when
        Exception exception = catchException(() -> reviewRepository.save(duplicateReview));

        // then
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);

    }
}
