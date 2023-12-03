package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.common.event.ImageDeletionEvent;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.RegisterReviewResponse;
import com.clova.anifriends.domain.review.exception.ApplicantNotFoundException;
import com.clova.anifriends.domain.review.exception.ReviewConflictException;
import com.clova.anifriends.domain.review.exception.ReviewNotFoundException;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.aspect.DataIntegrityHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private static final int REVIEW_BONUS_TEMPERATURE = 3;

    private final ReviewRepository reviewRepository;
    private final ApplicantRepository applicantRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ShelterNotificationRepository shelterNotificationRepository;
    private final ShelterRepository shelterRepository;
    private final VolunteerRepository volunteerRepository;

    @Transactional(readOnly = true)
    public FindReviewResponse findReview(Long userId, Long reviewId) {
        return FindReviewResponse.from(getReview(userId, reviewId));
    }

    @Transactional(readOnly = true)
    public FindShelterReviewsByShelterResponse findShelterReviewsByShelter(
        Long shelterId,
        Pageable pageable) {
        Shelter shelter = shelterRepository.findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("존재하지 않는 보호소입니다."));
        Page<Review> shelterReviewsByShelter = reviewRepository.findShelterReviewsByShelter(
            shelter, pageable);
        return FindShelterReviewsByShelterResponse.from(shelterReviewsByShelter);
    }

    @Transactional
    @DataIntegrityHandler(message = "이미 작성한 리뷰가 존재합니다.", exceptionClass = ReviewConflictException.class)
    public RegisterReviewResponse registerReview(
        Long volunteerId,
        Long applicationId,
        String content,
        List<String> imageUrls) {
        Applicant applicant = getApplicant(volunteerId, applicationId);
        Review review = new Review(applicant, content, imageUrls);
        applicant.increaseTemperature(REVIEW_BONUS_TEMPERATURE);
        reviewRepository.save(review);
        shelterNotificationRepository.save(makeNewReviewNotification(applicant));
        return RegisterReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public FindShelterReviewsResponse findShelterReviews(
        Long shelterId,
        Pageable pageable
    ) {
        Page<Review> reviewPage
            = reviewRepository.findAllByShelterId(shelterId, pageable);

        return FindShelterReviewsResponse.from(reviewPage);
    }

    @Transactional(readOnly = true)
    public FindVolunteerReviewsResponse findVolunteerReviews(Long volunteerId, Pageable pageable) {
        Page<Review> reviewPage
            = reviewRepository.findAllByVolunteerVolunteerIdOrderByCreatedAtDesc(volunteerId,
            pageable);
        return FindVolunteerReviewsResponse.of(reviewPage.getContent(), PageInfo.from(reviewPage));
    }

    @Transactional
    public void updateReview(
        Long volunteerId,
        Long reviewId,
        String content,
        List<String> imageUrls
    ) {
        Review review = getReview(volunteerId, reviewId);

        List<String> imagesToDelete = review.findImagesToDelete(imageUrls);
        applicationEventPublisher.publishEvent(new ImageDeletionEvent(imagesToDelete));

        review.updateReview(content, imageUrls);
    }

    @Transactional
    public void deleteReview(Long volunteerId, Long reviewId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        Review review = getReview(volunteerId, reviewId);
        volunteer.decreaseReviewCount();
        List<String> imagesToDelete = review.getImages();
        applicationEventPublisher.publishEvent(new ImageDeletionEvent(imagesToDelete));
        reviewRepository.delete(review);
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }

    private Review getReview(Long userId, Long reviewId) {
        return reviewRepository.findByReviewIdAndVolunteerId(reviewId, userId)
            .orElseThrow(() -> new ReviewNotFoundException("존재하지 않는 리뷰입니다."));
    }

    private Applicant getApplicant(Long userId, Long applicationId) {
        return applicantRepository.findByApplicantIdAndVolunteerId(
                applicationId, userId)
            .orElseThrow(() -> new ApplicantNotFoundException("봉사 신청 내역이 존재하지 않습니다."));
    }

    private ShelterNotification makeNewReviewNotification(Applicant applicant) {
        return new ShelterNotification(
            applicant.getRecruitment().getShelter(),
            applicant.getRecruitment().getTitle(),
            NotificationType.NEW_SHELTER_REVIEW.getMessage(),
            NotificationType.NEW_SHELTER_REVIEW.getName()
        );
    }
}
