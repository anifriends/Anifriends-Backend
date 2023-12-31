package com.clova.anifriends.domain.review.service;

import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicant;
import static com.clova.anifriends.domain.applicant.support.ApplicantFixture.applicantWithReview;
import static com.clova.anifriends.domain.applicant.vo.ApplicantStatus.ATTENDANCE;
import static com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture.recruitment;
import static com.clova.anifriends.domain.review.support.ReviewDtoFixture.findReviewResponse;
import static com.clova.anifriends.domain.review.support.ReviewFixture.review;
import static com.clova.anifriends.domain.shelter.support.ShelterFixture.shelter;
import static com.clova.anifriends.domain.volunteer.support.VolunteerFixture.volunteer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.common.util.EmailMasker;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse.FindShelterReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse.FindShelterReviewByVolunteerResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.exception.ApplicantNotFoundException;
import com.clova.anifriends.domain.review.exception.ReviewNotFoundException;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import com.clova.anifriends.domain.review.repository.response.FindShelterReviewByShelterResult;
import com.clova.anifriends.domain.review.support.ReviewFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.vo.VolunteerTemperature;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private ShelterNotificationRepository shelterNotificationRepository;

    @Mock
    ShelterRepository shelterRepository;

    @Mock
    VolunteerRepository volunteerRepository;


    @Nested
    @DisplayName("findReviewById 메서드 실행 시")
    class FindReviewByIdTest {

        @Test
        @DisplayName("성공")
        void findReview() {
            //given
            Shelter shelter = shelter();
            Volunteer volunteer = volunteer();
            Recruitment recruitment = recruitment(shelter);
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            Review review = review(applicant);
            FindReviewResponse expected = findReviewResponse(review);

            when(reviewRepository.findByReviewIdAndVolunteerId(anyLong(), anyLong()))
                .thenReturn(Optional.of(review));

            //when
            FindReviewResponse result = reviewService.findReview(anyLong(), anyLong());

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(NotFoundReviewException): 존재하지 않는 리뷰")
        void exceptionWhenReviewIsNotExist() {
            //given
            when(reviewRepository.findByReviewIdAndVolunteerId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

            //when
            Exception exception = catchException(
                () -> reviewService.findReview(anyLong(), anyLong()));

            //then
            assertThat(exception).isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("registerReview 메서드 실행 시")
    class RegisterReview {

        Shelter shelter;
        Volunteer volunteer;

        @BeforeEach
        void setUp() {
            shelter = shelter();
            volunteer = volunteer();
        }

        @Test
        @DisplayName("성공")
        void registerReview() {
            // given
            Applicant applicant = applicant(recruitment(shelter), volunteer, ATTENDANCE);

            when(applicantRepository.findByApplicantIdAndVolunteerId(anyLong(), anyLong()))
                .thenReturn(Optional.of(applicant));

            // when
            reviewService.registerReview(anyLong(), anyLong(), "reviewContent", null);

            // then
            verify(reviewRepository, times(1)).save(any(Review.class));
            verify(shelterNotificationRepository, times(1)).save(any(ShelterNotification.class));
        }


        @Test
        @DisplayName("성공: 봉사자의 리뷰 개수, 온도가 증가")
        void registerReviewThenIncreaseVolunteerReviewCount() {
            //given
            int originTemperature = 36;
            int reviewBonusTemperature = 3;
            ReflectionTestUtils.setField(volunteer, "temperature",
                new VolunteerTemperature(originTemperature));
            Recruitment recruitment = recruitment(shelter);
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);

            given(applicantRepository.findByApplicantIdAndVolunteerId(anyLong(), anyLong()))
                .willReturn(Optional.of(applicant));

            //when
            reviewService.registerReview(1L, 1L, "a".repeat(10), null);

            //then
            assertThat(volunteer.getTemperature())
                .isEqualTo(originTemperature + reviewBonusTemperature);
            assertThat(volunteer.getReviewCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("예외(ApplicantNotFoundException): 존재하지 않는 봉사 신청")
        void exceptionWhenApplicantNotFound() {
            // given
            when(applicantRepository.findByApplicantIdAndVolunteerId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> reviewService.registerReview(anyLong(), anyLong(), "reviewContent", null));

            // then
            assertThat(exception).isInstanceOf(ApplicantNotFoundException.class);
        }

        @Test
        @DisplayName("예외(ReviewBadRequestException): 이미 작성된 리뷰가 존재")
        void exceptionWhenAlreadyExistReview() {
            // given
            Shelter shelter = shelter();
            Applicant applicant = applicantWithReview(recruitment(shelter), volunteer());

            when(applicantRepository.findByApplicantIdAndVolunteerId(anyLong(), anyLong()))
                .thenReturn(Optional.of(applicant));
            when(reviewRepository.save(any(Review.class)))
                .thenThrow(DataIntegrityViolationException.class);
            // when
            Exception exception = catchException(
                () -> reviewService.registerReview(anyLong(), anyLong(), "reviewContent", null));

            // then
            assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("findShelterReviewsByShelter 메서드 실행 시")
    class FindShelterReviewsByShelterTest {

        @Test
        @DisplayName("성공")
        void findShelterReviews() {
            //given
            Long shelterId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Shelter shelter = shelter();
            FindShelterReviewByShelterResult result = new FindShelterReviewByShelterResult(
                1L, LocalDateTime.now(), "리뷰내용", 1L, "봉사자 이름", 36, "imageUrl", 0);
            PageImpl<FindShelterReviewByShelterResult> findShelterReviewByShelterResults = new PageImpl<>(
                List.of(result), pageRequest, 1);

            given(shelterRepository.findById(anyLong())).willReturn(Optional.of(shelter));
            given(reviewRepository.findShelterReviewsByShelter(any(), any()))
                .willReturn(findShelterReviewByShelterResults);

            //then
            FindShelterReviewsByShelterResponse response
                = reviewService.findShelterReviewsByShelter(shelterId, pageRequest);

            //then
            FindShelterReviewResponse findReview = response.reviews().get(0);
            assertThat(findReview.reviewContent()).isEqualTo(result.getReviewContent());
            assertThat(findReview.reviewImageUrls()).isEqualTo(result.getReviewImageUrls());
            assertThat(findReview.volunteerTemperature()).isEqualTo(
                result.getVolunteerTemperature());
            assertThat(findReview.volunteerReviewCount()).isEqualTo(
                result.getVolunteerReviewCount());
            assertThat(findReview.volunteerName()).isEqualTo(result.getVolunteerName());
            assertThat(findReview.volunteerImageUrl()).isEqualTo(result.getVolunteerImageUrl());
        }
    }

    @Nested
    @DisplayName("findVolunteerReviews 메서드 실행 시")
    class FindVolunteerReviewsTest {

        @Test
        @DisplayName("성공")
        void findVolunteerReviews() {
            // given
            Long volunteerId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            Review review = review(applicant);
            PageImpl<Review> reviewPage = new PageImpl<>(List.of(review));
            FindVolunteerReviewsResponse expected = FindVolunteerReviewsResponse.from(reviewPage);

            given(reviewRepository.findAllByVolunteerVolunteerIdOrderByCreatedAtDesc(anyLong(),
                any()))
                .willReturn(reviewPage);

            // when
            FindVolunteerReviewsResponse response
                = reviewService.findVolunteerReviews(volunteerId, pageRequest);

            // then
            assertThat(response).usingRecursiveComparison()
                .ignoringFields("reviewId")
                .isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findShelterReviews 메서드 실행 시")
    class FindShelterReviewsTest {

        @Test
        @DisplayName("성공")
        void findShelterReviews() {
            //given
            Long shelterId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            Review review = review(applicant);
            PageImpl<Review> reviewPage = new PageImpl<>(List.of(review));
            FindShelterReviewsResponse expected = FindShelterReviewsResponse.from(
                reviewPage);

            given(reviewRepository.findAllByShelterId(anyLong(), any()))
                .willReturn(reviewPage);

            //then
            FindShelterReviewsResponse response
                = reviewService.findShelterReviews(shelterId, pageRequest);

            //then
            assertThat(response).usingRecursiveComparison()
                .ignoringFields("reviewId")
                .isEqualTo(expected);
        }

        @Test
        @DisplayName("성공: 봉사자 이메일 마스킹 됨")
        void findShelterReviewsThenReturnMaskedEmail() {
            //given
            Long shelterId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Shelter shelter = shelter();
            Recruitment recruitment = recruitment(shelter);
            Volunteer volunteer = volunteer();
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            Review review = review(applicant);
            PageImpl<Review> reviewPage = new PageImpl<>(List.of(review));
            FindShelterReviewsResponse expected = FindShelterReviewsResponse.from(
                reviewPage);

            given(reviewRepository.findAllByShelterId(anyLong(), any()))
                .willReturn(reviewPage);

            //then
            FindShelterReviewsResponse response
                = reviewService.findShelterReviews(shelterId, pageRequest);

            //then
            assertThat(response.reviews()).hasSize(1);
            FindShelterReviewByVolunteerResponse findReview = response.reviews().get(0);
            assertThat(findReview.volunteerEmail()).isEqualTo(
                EmailMasker.masking(volunteer.getEmail()));
        }
    }

    @Nested
    @DisplayName("updateReview 메서드 실행 시")
    class UpdateReviewTest {

        @Test
        @DisplayName("성공")
        void updateReview() {
            // given
            Recruitment recruitment = recruitment(shelter());
            Volunteer volunteer = volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer, ATTENDANCE);
            List<String> originalImageUrls = List.of("url1", "url2");
            Review review = ReviewFixture.review(applicant, originalImageUrls);
            String newContent = "updatedContent";
            List<String> newImageUrls = List.of("url3", "url4");

            given(reviewRepository.findByReviewIdAndVolunteerId(anyLong(), anyLong()))
                .willReturn(Optional.of(review));

            // when
            reviewService.updateReview(anyLong(), anyLong(), newContent, newImageUrls);

            // then
            verify(applicationEventPublisher, times(1)).publishEvent(
                new ImageDeletionEvent(originalImageUrls));
            assertThat(review.getContent()).isEqualTo(newContent);
            assertThat(review.getImages()).isEqualTo(newImageUrls);
        }

        @Test
        @DisplayName("예외(ReviewNotFoundException): 존재하지 않는 리뷰")
        void exceptionWhenReviewIsNotExist() {
            // given
            given(reviewRepository.findByReviewIdAndVolunteerId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> reviewService.updateReview(anyLong(), anyLong(), "content", null));

            // then
            assertThat(exception).isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteReview 메서드 호출 시")
    class DeleteReviewTest {

        Shelter shelter;
        Volunteer volunteer;
        Recruitment recruitment;

        @BeforeEach
        void setUp() {
            shelter = shelter();
            volunteer = volunteer();
            recruitment = recruitment(shelter);
        }

        @Test
        @DisplayName("성공")
        void deleteReview() {
            //given
            Applicant applicant = applicant(recruitment, volunteer, ATTENDANCE);
            Review review = review(applicant);

            given(volunteerRepository.findById(anyLong())).willReturn(Optional.of(volunteer));
            given(reviewRepository.findByReviewIdAndVolunteerId(anyLong(), anyLong()))
                .willReturn(Optional.of(review));

            //when
            reviewService.deleteReview(1L, 1L);

            //then
            then(reviewRepository).should().delete(any(Review.class));
        }

        @Test
        @DisplayName("예외(ReviewNotFoundException): 존재하지 않는 봉사 후기")
        void exceptionWhenReviewNotFound() {
            //given
            given(volunteerRepository.findById(anyLong())).willReturn(Optional.of(volunteer));
            given(reviewRepository.findByReviewIdAndVolunteerId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> reviewService.deleteReview(1L, 1L));

            //then
            assertThat(exception).isInstanceOf(ReviewNotFoundException.class);
        }

        @Test
        @DisplayName("예외(VolunteerNotFoundException): 존재하지 않는 봉사자")
        void exceptionWhenVolunteerNotFound() {
            //given
            given(volunteerRepository.findById(anyLong())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> reviewService.deleteReview(1L, 1L));

            //then
            assertThat(exception).isInstanceOf(VolunteerNotFoundException.class);
        }
    }
}
