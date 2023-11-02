package com.clova.anifriends.domain.review.service;

import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.exception.ReviewNotFoundException;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
