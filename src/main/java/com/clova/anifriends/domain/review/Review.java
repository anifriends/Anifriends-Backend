package com.clova.anifriends.domain.review;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.review.exception.ReviewAuthorizationException;
import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import com.clova.anifriends.domain.review.vo.ReviewContent;
import com.clova.anifriends.domain.volunteer.Volunteer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    private static final int REVIEW_IMAGE_URLS_SIZE = 5;

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", unique = true)
    private Applicant applicant;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    @Embedded
    private ReviewContent content;

    public Review(
        Applicant applicant,
        String content,
        List<String> images
    ) {
        validateApplicant(applicant);
        validateImageUrlsSize(images);
        this.applicant = applicant;
        this.applicant.registerReview(this);
        this.content = new ReviewContent(content);
        if (Objects.nonNull(images)) {
            List<ReviewImage> newImages = images.stream()
                .map(url -> new ReviewImage(this, url))
                .toList();
            this.images.addAll(newImages);
        }
    }

    private void validateImageUrlsSize(List<String> imageUrls) {
        if (imageUrls != null && imageUrls.size() > REVIEW_IMAGE_URLS_SIZE) {
            throw new ReviewBadRequestException(
                MessageFormat.format("리뷰 이미지는 최대 {0}개까지 등록할 수 있습니다.", REVIEW_IMAGE_URLS_SIZE)
            );
        }
    }

    private void validateApplicant(Applicant applicant) {
        if (applicant.getStatus() != ApplicantStatus.ATTENDANCE) {
            throw new ReviewAuthorizationException("봉사에 출석한 사용자만 리뷰를 작성할 수 있습니다.");
        }
    }

    public void updateReview(
        String content,
        List<String> imageUrls
    ) {
        this.content = this.content.updateContent(content);
        updateImageUrls(imageUrls);
    }

    public List<String> findImagesToDelete(List<String> imageUrls) {
        if (Objects.isNull(imageUrls)) {
            return getImages();
        }
        return this.images.stream()
            .map(ReviewImage::getImageUrl)
            .filter(existsImageUrl -> !imageUrls.contains(existsImageUrl))
            .toList();
    }

    private void updateImageUrls(List<String> imageUrls) {
        if (Objects.nonNull(imageUrls)) {
            validateImageUrlsSize(imageUrls);
            addNewImageUrls(imageUrls);
        }
    }

    private void addNewImageUrls(List<String> updateImageUrls) {
        List<ReviewImage> existsReviewImages = filterRemainImages(updateImageUrls);
        List<ReviewImage> newReviewImages = filterNewImages(updateImageUrls);

        List<ReviewImage> newImages = new ArrayList<>();
        newImages.addAll(existsReviewImages);
        newImages.addAll(newReviewImages);

        this.images = newImages;
    }

    private List<ReviewImage> filterRemainImages(List<String> updateImageUrls) {
        return this.images.stream()
            .filter(reviewImage -> updateImageUrls.contains(reviewImage.getImageUrl()))
            .toList();
    }

    private List<ReviewImage> filterNewImages(
        List<String> updateImageUrls
    ) {
        List<String> existsImageUrls = getImages();

        return updateImageUrls.stream()
            .filter(imageUrl -> !existsImageUrls.contains(imageUrl))
            .map(imageUrl -> new ReviewImage(this, imageUrl))
            .toList();
    }

    public String getContent() {
        return content.getContent();
    }

    public List<String> getImages() {
        return images.stream()
            .map(ReviewImage::getImageUrl)
            .toList();
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Volunteer getVolunteer() {
        return applicant.getVolunteer();
    }

}
