package com.clova.anifriends.domain.review;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.review.wrapper.Content;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "review")
public class Review {

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

    @Embedded
    private Content content;

    protected Review() {
    }

    public Review(
        Recruitment recruitment,
        Volunteer volunteer,
        String content
    ) {
        this.recruitment = recruitment;
        this.volunteer = volunteer;
        this.content = new Content(content);
    }

}
