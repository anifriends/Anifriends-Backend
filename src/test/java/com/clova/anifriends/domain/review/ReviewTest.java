package com.clova.anifriends.domain.review;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus.PENDING;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.exception.ReviewAuthorizationException;
import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
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
}
