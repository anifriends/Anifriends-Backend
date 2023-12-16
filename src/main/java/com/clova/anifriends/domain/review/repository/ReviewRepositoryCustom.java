package com.clova.anifriends.domain.review.repository;

import com.clova.anifriends.domain.review.repository.response.FindShelterReviewByShelterResult;
import com.clova.anifriends.domain.shelter.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface ReviewRepositoryCustom {

    Page<FindShelterReviewByShelterResult> findShelterReviewsByShelter(
        @Param("shelter") Shelter shelter, Pageable pageable);
}
