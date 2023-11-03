package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.exception.ReviewNotFoundException;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public FindReviewResponse findReview(Long userId, Long reviewId) {
        return FindReviewResponse.from(getReview(userId, reviewId));
    }

    private Review getReview(Long userId, Long reviewId) {
        return reviewRepository.findByReviewIdAndVolunteerId(reviewId, userId)
            .orElseThrow(() -> new ReviewNotFoundException("존재하지 않는 리뷰입니다."));
    }

    @Transactional(readOnly = true)
    public FindShelterReviewsResponse findShelterReviews(Long shelterId, Pageable pageable) {
        Page<Review> reviewPage
            = reviewRepository.findAllByRecruitmentShelterShelterId(shelterId, pageable);
        return FindShelterReviewsResponse.from(reviewPage);
    }
}
