package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.common.dto.PageInfo;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.exception.ApplicantNotFoundException;
import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import com.clova.anifriends.domain.review.exception.ReviewNotFoundException;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ApplicantRepository applicantRepository;

    private final ImageRemover imageRemover;

    @Transactional(readOnly = true)
    public FindReviewResponse findReview(Long userId, Long reviewId) {
        return FindReviewResponse.from(getReview(userId, reviewId));
    }

    @Transactional(readOnly = true)
    public FindShelterReviewsByShelterResponse findShelterReviewsByShelter(
        Long shelterId,
        Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAllByShelterId(shelterId, pageable);
        return FindShelterReviewsByShelterResponse.from(reviewPage);
    }

    @Transactional
    public Long registerReview(Long userId, Long applicationId, String content,
        List<String> imageUrls) {
        Applicant applicant = getApplicant(userId, applicationId);

        validateNotExistReview(applicant);
        Review review = new Review(applicant, content, imageUrls);
        reviewRepository.save(review);

        return review.getReviewId();
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
        imageRemover.deleteImages(review.findImagesToDelete(imageUrls));
        review.updateReview(content, imageUrls);
    }

    @Transactional
    public void deleteReview(Long volunteerId, Long reviewId) {
        Review review = getReview(volunteerId, reviewId);
        imageRemover.deleteImages(review.getImages());
        reviewRepository.delete(review);
    }

    private Review getReview(Long userId, Long reviewId) {
        return reviewRepository.findByReviewIdAndVolunteerId(reviewId, userId)
            .orElseThrow(() -> new ReviewNotFoundException("존재하지 않는 리뷰입니다."));
    }

    private void validateNotExistReview(Applicant applicant) {
        if (applicant.hasReview()) {
            throw new ReviewBadRequestException("이미 작성된 리뷰가 존재합니다.");
        }
    }

    private Applicant getApplicant(Long userId, Long applicationId) {
        return applicantRepository.findByApplicantIdAndVolunteerId(
                applicationId, userId)
            .orElseThrow(() -> new ApplicantNotFoundException("봉사 신청 내역이 존재하지 않습니다."));
    }
}
