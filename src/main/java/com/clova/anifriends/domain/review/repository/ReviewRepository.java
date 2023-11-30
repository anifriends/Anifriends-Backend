package com.clova.anifriends.domain.review.repository;

import com.clova.anifriends.domain.review.Review;
import com.clova.anifriends.domain.review.repository.response.FindShelterReviewResult;
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


    @Query("select "
        + "r.reviewId as reviewId, "
        + "r.createdAt as createdAt, "
        + "r.content.content as content, "
        + "r.images as reviewImages, "
        + "v.volunteerId as volunteerId, "
        + "v.name.name as volunteerName, "
        + "v.temperature.temperature as temperature, "
        + "(select count(r2.reviewId) from Review r2 "
        + "join r2.applicant a2 "
        + "where a2.volunteer.volunteerId = v.volunteerId) as volunteerReviewCount, "
        + "i.imageUrl as volunteerImageUrl "
        + "from Review r "
        + "join r.applicant a "
        + "join a.volunteer v "
        + "left join v.image i "
        + "where a.recruitment.shelter.shelterId = :shelterId")
    Page<FindShelterReviewResult> findShelterReviewsByShelter(@Param("shelterId") Long shelterId,
        Pageable pageable);
}
