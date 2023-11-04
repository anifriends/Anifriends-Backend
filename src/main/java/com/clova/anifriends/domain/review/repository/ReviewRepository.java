package com.clova.anifriends.domain.review.repository;

import com.clova.anifriends.domain.review.Review;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r "
        + "join fetch r.applicant a "
        + "where r.reviewId = :reviewId and a.volunteer.volunteerId = :volunteerId")
    Optional<Review> findByReviewIdAndVolunteerId(
        @Param("reviewId") Long reviewId,
        @Param("userId") Long userId);

    Page<Review> findAllByRecruitmentShelterShelterId(Long shelterId, Pageable pageable);
}
