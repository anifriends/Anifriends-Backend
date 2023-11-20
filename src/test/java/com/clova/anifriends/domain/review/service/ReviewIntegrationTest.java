package com.clova.anifriends.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.ReviewImage;
import com.clova.anifriends.domain.review.exception.ReviewConflictException;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.clova.anifriends.global.image.S3Service;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ReviewIntegrationTest extends BaseIntegrationTest {

    @Autowired
    ReviewService reviewService;

    @MockBean
    S3Service s3Service;

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
            Review review = ReviewFixture.review(applicant);
            review.updateReview(null, List.of("image1", "image2"));
            reviewRepository.save(review);

            //when
            reviewService.deleteReview(volunteer.getVolunteerId(), review.getReviewId());

            //then
            verify(s3Service, times(1)).deleteImages(List.of("image1", "image2"));
            Review findReview = entityManager.find(Review.class, review.getReviewId());
            assertThat(findReview).isNull();
            List<ReviewImage> findReviewImages = entityManager.createQuery(
                    "select ri from ReviewImage ri", ReviewImage.class)
                .getResultList();
            assertThat(findReviewImages).isEmpty();
        }
    }

    @Nested
    @DisplayName("registerReview 메서드 호출 시")
    class RegisterReviewTest {

        @Test
        @DisplayName("예외(ReviewConflictException): 중복된 후기")
        void exceptionWhenDuplicateReview() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Volunteer volunteer = VolunteerFixture.volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer,
                ApplicantStatus.ATTENDANCE);

            shelterRepository.save(shelter);
            recruitmentRepository.save(recruitment);
            volunteerRepository.save(volunteer);
            applicantRepository.save(applicant);

            Review review = ReviewFixture.review(applicant);
            reviewRepository.save(review);

            // when
            Exception exception = catchException(
                () -> reviewService.registerReview(volunteer.getVolunteerId(),
                    applicant.getApplicantId(),
                    "강아지들 진짜 귀여워요 나만 없어 강아지..", List.of("image1", "image2")));

            // then
            assertThat(exception).isInstanceOf(ReviewConflictException.class);

        }
    }
}
