package com.clova.anifriends.domain.review;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.PENDING;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.exception.ReviewAuthorizationException;
import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Nested
    @DisplayName("Review 생성 시")
    class NewReviewTest {

        @Test
        @DisplayName("성공")
        void newReview() {
            //given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            String content = "1234567890";

            //when
            Review review = new Review(applicant, content, null);

            //then
            assertThat(review.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("예외(ReviewBadRequestException): imageUrls 가 5개 초과")
        void exceptionWhenImageUrlsOver() {
            //given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            String content = "1234567890";
            String imageUrl1 = "www.aws.s3.com/1";
            String imageUrl2 = "www.aws.s3.com/2";
            String imageUrl3 = "www.aws.s3.com/3";
            String imageUrl4 = "www.aws.s3.com/4";
            String imageUrl5 = "www.aws.s3.com/5";
            String imageUrl6 = "www.aws.s3.com/6";
            List<String> imageUrls = List.of(
                imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6
            );

            //when
            Exception exception = catchException(() -> new Review(applicant, content, imageUrls));

            //then
            assertThat(exception).isInstanceOf(ReviewBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ReviewAuthorizationException): 봉사에 출석한 사람만 리뷰 작성 가능")
        void exceptionWhenNotAttendance() {
            //given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, PENDING);
            String content = "1234567890";

            //when
            Exception exception = catchException(() -> new Review(applicant, content, null));

            //then
            assertThat(exception).isInstanceOf(ReviewAuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("updateReview 메서드 호출 시")
    class UpdateReviewTest {

        Review review;

        @BeforeEach
        void setUp() {
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer, ATTENDANCE);
            review = ReviewFixture.review(applicant);
        }

        @Test
        @DisplayName("성공: 모든 인자가 null 아닐 때")
        void updateReview() {
            //given
            String content = "1234567890";
            String imageUrl1 = "www.aws.s3.com/1";
            String imageUrl2 = "www.aws.s3.com/2";
            String imageUrl3 = "www.aws.s3.com/3";
            String imageUrl4 = "www.aws.s3.com/4";
            String imageUrl5 = "www.aws.s3.com/5";
            List<String> imageUrls = List.of(
                imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5
            );

            //when
            review.updateReview(content, imageUrls);

            //then
            assertThat(review.getContent()).isEqualTo(content);
            assertThat(review.getImages()).isEqualTo(imageUrls);
        }

        @Test
        @DisplayName("성공: 모든 인지가 null일 때")
        void updateReviewWhenAllNull() {
            //given
            String content = null;
            List<String> imageUrls = null;
            String givenContent = review.getContent();
            List<String> givenImageUrls = review.getImages();

            //when
            review.updateReview(content, imageUrls);

            //then
            assertThat(review.getContent()).isEqualTo(givenContent);
            assertThat(review.getImages()).isEqualTo(givenImageUrls);
        }

        @Test
        @DisplayName("성공: 이미지 리스트가 비어있을 떄 모든 이미지를 삭제한다.")
        void updateReviewWhenImageUrlsEmpty() {
            //given
            String content = "1234567890";
            List<String> imageUrls = List.of();

            //when
            review.updateReview(content, imageUrls);

            //then
            assertThat(review.getContent()).isEqualTo(content);
            assertThat(review.getImages()).isEmpty();
        }

        @Test
        @DisplayName("예외(ReviewBadRequestException): 이미지 리스트가 5장을 초과했을 떄")
        void throwExceptionWhenImageIsOver5() {
            // given
            String content = "1234567890";
            List<String> imageUrls = List.of(
                "www.aws.s3.com/1",
                "www.aws.s3.com/2",
                "www.aws.s3.com/3",
                "www.aws.s3.com/4",
                "www.aws.s3.com/5",
                "www.aws.s3.com/6"
            );

            // when
            Exception exception = catchException(
                () -> review.updateReview(content, imageUrls));

            // then
            assertThat(exception).isInstanceOf(ReviewBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("findImagesToDelete 메서드 호출 시")
    class FindImagesToDelete {

        @Test
        @DisplayName("성공: 기존 이미지 2개, 유지 이미지 0개, 새로운 이미지 0개")
        void findImagesToDelete1() {
            // given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);

            String imageUrl1 = "www.aws.s3.com/1";
            String imageUrl2 = "www.aws.s3.com/2";
            List<String> existsImageUrls = List.of(imageUrl1, imageUrl2);
            List<String> newImageUrls = List.of();

            Review review = ReviewFixture.review(applicant, existsImageUrls);

            // when
            List<String> result = review.findImagesToDelete(newImageUrls);

            // then
            assertThat(result).isEqualTo(existsImageUrls);
        }

        @Test
        @DisplayName("성공: 기존 이미지 0개, 유지 이미지 0개, 새로운 이미지 0개")
        void findImagesToDelete2() {
            // given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);

            List<String> existsImageUrls = List.of();
            List<String> newImageUrls = List.of();

            Review review = ReviewFixture.review(applicant, existsImageUrls);

            // when
            List<String> result = review.findImagesToDelete(newImageUrls);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공: 기존 이미지 2개, 유지 이미지 1개, 새로운 이미지 1개")
        void findImagesToDelete3() {
            // given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);

            String imageUrl1 = "www.aws.s3.com/1";
            String imageUrl2 = "www.aws.s3.com/2";
            String newImageUrl = imageUrl2;

            List<String> existsImageUrls = List.of(imageUrl1, imageUrl2);
            List<String> newImageUrls = List.of(newImageUrl);

            Review review = ReviewFixture.review(applicant, existsImageUrls);

            // when
            List<String> result = review.findImagesToDelete(newImageUrls);

            // then
            assertThat(result).isEqualTo(List.of(imageUrl1));
        }
    }
}
