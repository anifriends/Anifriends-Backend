package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.exception.ApplicantNotFoundException;
import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import com.clova.anifriends.domain.review.exception.ReviewNotFoundException;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ApplicantRepository applicantRepository;

    public FindReviewResponse findReview(Long userId, Long reviewId) {
        return FindReviewResponse.from(getReview(userId, reviewId));
    }

    private Review getReview(Long userId, Long reviewId) {
        return reviewRepository.findByReviewIdAndVolunteerId(reviewId, userId)
            .orElseThrow(() -> new ReviewNotFoundException("존재하지 않는 리뷰입니다."));
    }

    public Long registerReview(Long userId, Long applicationId, String content,
        List<String> imageUrls) {
        Applicant applicant = getApplicant(userId, applicationId);

        validateNotExistReview(applicant);
        Review review = new Review(applicant, content, imageUrls);
        reviewRepository.save(review);

        return review.getReviewId();
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
