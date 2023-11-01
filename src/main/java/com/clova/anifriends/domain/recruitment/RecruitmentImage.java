package com.clova.anifriends.domain.recruitment;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recruitment_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentImage extends BaseTimeEntity {

    @Id
    @Column(name = "recruitment_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruitmentImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Column(name = "image_url")
    private String imageUrl;

    public RecruitmentImage(Recruitment recruitment, String imageUrl) {
        this.recruitment = recruitment;
        this.imageUrl = imageUrl;
    }
}
