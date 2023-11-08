package com.clova.anifriends.domain.review.controller;

import com.clova.anifriends.domain.auth.resolver.LoginUser;
import com.clova.anifriends.domain.review.dto.request.RegisterReviewRequest;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByVolunteerResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.service.ReviewService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/volunteers/reviews")
    public ResponseEntity<Void> registerReview(
        @LoginUser Long userId,
        @RequestBody RegisterReviewRequest request
    ) {
        long reviewId = reviewService.registerReview(userId, request.applicationId(),
            request.content(), request.imageUrls());
        URI location = URI.create("/api/volunteers/reviews/" + reviewId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/shelters/{shelterId}/reviews")
    public ResponseEntity<FindShelterReviewsByShelterResponse> findShelterReviewsByShelter(
        @PathVariable("shelterId") Long shelterId,
        Pageable pageable) {
        FindShelterReviewsByShelterResponse response
            = reviewService.findShelterReviewsByShelter(shelterId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/volunteers/{volunteerId}/reviews")
    public ResponseEntity<FindVolunteerReviewsResponse> findVolunteerReviews(
        @PathVariable("volunteerId") Long volunteerId,
        Pageable pageable,
        @LoginUser Long userId
    ) {
        return ResponseEntity.ok(reviewService.findVolunteerReviews(volunteerId, pageable));
    }

    @GetMapping("/volunteers/shelters/{shelterId}/reviews")
    public ResponseEntity<FindShelterReviewsByVolunteerResponse> findShelterReviewsByVolunteer(
        @PathVariable("shelterId") Long shelterId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            reviewService.findShelterReviewsByVolunteer(shelterId, pageable)
        );
    }
}
