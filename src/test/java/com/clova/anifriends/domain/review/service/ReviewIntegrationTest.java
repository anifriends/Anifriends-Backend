package com.clova.anifriends.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.base.MockImageRemover;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.ReviewImage;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewIntegrationTest extends BaseIntegrationTest {

    @Autowired
    ReviewService reviewService;

    @Nested
    @DisplayName("deleteReview 메서드 호출 시")
    class DeleteReviewTest {

        Shelter shelter;
        Recruitment recruitment;
        Volunteer volunteer;
        Applicant applicant;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter();
            recruitment = RecruitmentFixture.recruitment(shelter);
            volunteer = VolunteerFixture.volunteer();
            applicant = ApplicantFixture.applicant(recruitment, volunteer,
                ApplicantStatus.ATTENDANCE);
            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);
            volunteerRepository.save(volunteer);
            applicantRepository.save(applicant);
        }

        @Test
        @DisplayName("성공")
        void deleteReview() {
            //given
            ImageRemover imageRemover = new MockImageRemover();
            Review review = ReviewFixture.review(applicant);
            review.updateReview(null, List.of("image1", "image2"), imageRemover);
            reviewRepository.save(review);

            //when
            reviewService.deleteReview(volunteer.getVolunteerId(), review.getReviewId());

            //then
            Review findReview = entityManager.find(Review.class, review.getReviewId());
            assertThat(findReview).isNull();
            List<ReviewImage> findReviewImages = entityManager.createQuery(
                    "select ri from ReviewImage ri", ReviewImage.class)
                .getResultList();
            assertThat(findReviewImages).isEmpty();
        }
    }
}
