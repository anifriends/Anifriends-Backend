package com.clova.anifriends.domain.review.repository;

import com.clova.anifriends.domain.review.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.reviewId = :reviewId and r.volunteer.volunteerId = :userId")
    Optional<Review> findByReviewIdAndVolunteerId(Long reviewId, Long userId);
}
