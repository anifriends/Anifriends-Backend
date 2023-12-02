package com.clova.anifriends.domain.review.repository;

import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.shelter.Shelter;
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
        @Param("volunteerId") Long volunteerId
    );

    @Query("select r from Review r "
        + "join fetch r.applicant a "
        + "join fetch a.recruitment rc "
        + "where rc.shelter.shelterId = :shelterId")
    Page<Review> findAllByShelterId(Long shelterId, Pageable pageable);

    @Query("select r from Review r "
        + "join fetch r.applicant a "
        + "where a.volunteer.volunteerId = :volunteerId")
    Page<Review> findAllByVolunteerVolunteerIdOrderByCreatedAtDesc(
        @Param("volunteerId") Long volunteerId,
        Pageable pageable);

    @Query("select r from Review r"
        + " left join r.images"
        + " join r.applicant.volunteer v"
        + " left join v.image"
        + " where r.applicant.recruitment.shelter = :shelter")
    Page<Review> findShelterReviewsByShelter(@Param("shelter") Shelter shelter,
        Pageable pageable);
}
