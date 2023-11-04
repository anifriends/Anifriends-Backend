package com.clova.anifriends.domain.review.repository;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

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
}
