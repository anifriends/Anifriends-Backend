package com.clova.anifriends.domain.review;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.volunteer.Volunteer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_image")
public class ReviewImage extends BaseTimeEntity {

    @Id
    @Column(name = "review_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name = "image_url")
    private String imageUrl;

}
