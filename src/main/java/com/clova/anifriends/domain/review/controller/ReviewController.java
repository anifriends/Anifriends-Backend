package com.clova.anifriends.domain.review.controller;

import com.clova.anifriends.domain.auth.resolver.LoginUser;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/volunteers/reviews/{reviewId}")
    public ResponseEntity<FindReviewResponse> findReview(
        @LoginUser Long userId, @PathVariable Long reviewId
    ) {
        return ResponseEntity.ok(reviewService.findReview(userId, reviewId));
    }

    @GetMapping("/shelters/{shelterId}/reviews")
    public ResponseEntity<FindShelterReviewsResponse> findShelterReviews(
        @PathVariable("shelterId") Long shelterId,
        Pageable pageable) {
        FindShelterReviewsResponse response = reviewService.findShelterReviews(shelterId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/volunteers/{volunteerId}/reviews")
    public ResponseEntity<FindVolunteerReviewsResponse> findVolunteerReviews(
        @PathVariable("volunteerId") Long volunteerId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.findVolunteerReviews(volunteerId, pageable));
    }
}
