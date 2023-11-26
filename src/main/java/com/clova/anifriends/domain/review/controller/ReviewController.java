package com.clova.anifriends.domain.review.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.review.dto.request.RegisterReviewRequest;
import com.clova.anifriends.domain.review.dto.request.UpdateReviewRequest;
import com.clova.anifriends.domain.review.dto.response.FindReviewResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.FindShelterReviewsByShelterResponse;
import com.clova.anifriends.domain.review.dto.response.FindVolunteerReviewsResponse;
import com.clova.anifriends.domain.review.dto.response.RegisterReviewResponse;
import com.clova.anifriends.domain.review.service.ReviewService;
import com.clova.anifriends.domain.auth.authorization.ShelterOnly;
import com.clova.anifriends.domain.auth.authorization.VolunteerOnly;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @VolunteerOnly
    @GetMapping("/volunteers/reviews/{reviewId}")
    public ResponseEntity<FindReviewResponse> findReview(
        @LoginUser Long volunteerId,
        @PathVariable Long reviewId
    ) {
        return ResponseEntity.ok(reviewService.findReview(volunteerId, reviewId));
    }

    @VolunteerOnly
    @PostMapping("/volunteers/reviews")
    public ResponseEntity<RegisterReviewResponse> registerReview(
        @LoginUser Long volunteerId,
        @RequestBody RegisterReviewRequest request
    ) {
        RegisterReviewResponse registerReviewResponse = reviewService.registerReview(
            volunteerId,
            request.applicationId(),
            request.content(),
            request.imageUrls());
        URI location = URI.create("/api/volunteers/reviews/" + registerReviewResponse.reviewId());
        return ResponseEntity.created(location).body(registerReviewResponse);
    }

    @ShelterOnly
    @GetMapping("/shelters/me/reviews")
    public ResponseEntity<FindShelterReviewsByShelterResponse> findShelterReviewsByShelter(
        @LoginUser Long shelterId,
        Pageable pageable) {
        FindShelterReviewsByShelterResponse response = reviewService.findShelterReviewsByShelter(shelterId, pageable);
        return ResponseEntity.ok(response);
    }

    @ShelterOnly
    @GetMapping("/shelters/volunteers/{volunteerId}/reviews")
    public ResponseEntity<FindVolunteerReviewsResponse> findVolunteerReviewsByShelter(
        @PathVariable("volunteerId") Long volunteerId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.findVolunteerReviews(volunteerId, pageable));
    }

    @VolunteerOnly
    @GetMapping("/volunteers/me/reviews")
    public ResponseEntity<FindVolunteerReviewsResponse> findVolunteerReviewsByVolunteers(
        @LoginUser Long volunteerId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.findVolunteerReviews(volunteerId, pageable));
    }

    @GetMapping("/shelters/{shelterId}/reviews")
    public ResponseEntity<FindShelterReviewsResponse> findShelterReviews(
        @PathVariable("shelterId") Long shelterId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            reviewService.findShelterReviews(shelterId, pageable)
        );
    }

    @VolunteerOnly
    @PatchMapping("/volunteers/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(
        @LoginUser Long volunteerId,
        @PathVariable("reviewId") Long reviewId,
        @RequestBody @Valid UpdateReviewRequest updateReviewRequest
    ) {
        reviewService.updateReview(volunteerId, reviewId, updateReviewRequest.content(),
            updateReviewRequest.imageUrls());
        return ResponseEntity.noContent().build();
    }

    @VolunteerOnly
    @DeleteMapping("/volunteers/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
        @LoginUser Long volunteerId,
        @PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(volunteerId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
