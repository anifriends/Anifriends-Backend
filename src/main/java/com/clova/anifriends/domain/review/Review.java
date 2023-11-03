package com.clova.anifriends.domain.review;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.wrapper.ApplicantStatus;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.exception.ReviewAuthorizationException;
import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import com.clova.anifriends.domain.review.wrapper.ReviewContent;
import com.clova.anifriends.domain.volunteer.Volunteer;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    public static final int REVIEW_IMAGE_URLS_SIZE = 5;
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @JoinColumn(name = "recruitment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Recruitment recruitment;

    @JoinColumn(name = "volunteer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Volunteer volunteer;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReviewImage> imageUrls = new ArrayList<>();

    @Embedded
    private ReviewContent content;

    public Review(
        Applicant applicant,
        String content,
        List<String> imageUrls
    ) {
        validateApplicant(applicant);
        validateImageUrlsSize(imageUrls);
        this.recruitment = applicant.getRecruitment();
        this.volunteer = applicant.getVolunteer();
        this.content = new ReviewContent(content);
        this.imageUrls = imageUrls == null ? null : imageUrls.stream()
            .map(url -> new ReviewImage(this, url))
            .toList();
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

    public String getContent() {
        return content.getContent();
    }

    public List<String> getImageUrls() {
        return imageUrls.stream()
            .map(ReviewImage::getImageUrl)
            .toList();
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Recruitment getRecruitment() {
        return recruitment;
    }
}
