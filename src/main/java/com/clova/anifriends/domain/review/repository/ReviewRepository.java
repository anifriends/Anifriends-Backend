package com.clova.anifriends.domain.review.repository;

import com.clova.anifriends.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
