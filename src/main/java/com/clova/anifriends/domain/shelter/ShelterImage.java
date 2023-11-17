package com.clova.anifriends.domain.shelter;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shelter_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterImage extends BaseTimeEntity {

    @Id
    @Column(name = "shelter_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shelterImageId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Column(name = "image_url")
    private String imageUrl;

    public ShelterImage(
        Shelter shelter,
        String imageUrl
    ) {
        this.shelter = shelter;
        this.imageUrl = imageUrl;
    }

    public boolean isDifferentFrom(String imageUrl) {
        return !this.imageUrl.equals(imageUrl);
    }

    public boolean isSameWith(String imageUrl) {
        return this.imageUrl.equals(imageUrl);
    }
}
