package com.clova.anifriends.domain.review;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.recruitment.Recruitment;
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
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

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
        Recruitment recruitment,
        Volunteer volunteer,
        String content
    ) {
        this.recruitment = recruitment;
        this.volunteer = volunteer;
        this.content = new ReviewContent(content);
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
