package com.clova.anifriends.domain.review.repository;

import static com.clova.anifriends.domain.review.QReviewImage.reviewImage;

import com.clova.anifriends.domain.review.ReviewImage;
import com.clova.anifriends.domain.review.repository.response.FindShelterReviewByShelterResult;
import com.clova.anifriends.domain.shelter.Shelter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public Page<FindShelterReviewByShelterResult> findShelterReviewsByShelter(Shelter shelter,
        Pageable pageable) {
        List<FindShelterReviewByShelterResult> content 
            = getShelterReviewsByShelter(shelter, pageable);
        Long count = em.createQuery(
                "select count(r.reviewId) from Review r"
                    + " join r.applicant a"
                    + " join a.recruitment ri"
                    + " where ri.shelter = :shelter", Long.class)
            .setParameter("shelter", shelter)
            .getSingleResult();

        List<Long> reviewIds = getReviewIds(content);
        Map<Long, List<ReviewImage>> collect = getReviewImagesIn(reviewIds);
        content.forEach(result -> result.setReviewImageUrls(
            collect.get(result.getReviewId()).stream()
                .map(ReviewImage::getImageUrl)
                .toList()));
        return new PageImpl<>(content, pageable, count != null ? count : 0);
    }

    private List<FindShelterReviewByShelterResult> getShelterReviewsByShelter(
        Shelter shelter,
        Pageable pageable) {
        return em.createQuery(
                "select new com.clova.anifriends.domain.review.repository.response"
                    + ".FindShelterReviewByShelterResult(r.reviewId,"
                    + " r.createdAt,"
                    + " r.content.content,"
                    + " v.volunteerId,"
                    + " v.name.name,"
                    + " v.temperature.temperature,"
                    + " vi.imageUrl,"
                    + " v.volunteerReviewCount.reviewCount)"
                    + " from Review r"
                    + " join r.applicant a"
                    + " join a.volunteer v"
                    + " left join v.image vi"
                    + " where a.recruitment.shelter = :shelter",
                FindShelterReviewByShelterResult.class)
            .setParameter("shelter", shelter)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
    }

    private List<Long> getReviewIds(List<FindShelterReviewByShelterResult> content) {
        return content.stream()
            .map(FindShelterReviewByShelterResult::getReviewId)
            .toList();
    }

    private Map<Long, List<ReviewImage>> getReviewImagesIn(List<Long> reviewIds) {
        List<ReviewImage> reviewImages = query
            .selectFrom(reviewImage)
            .where(reviewImage.review.reviewId.in(reviewIds))
            .fetch();
        return reviewImages.stream().collect(Collectors.groupingBy(ReviewImage::getReviewId));
    }
}
