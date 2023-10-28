package com.clova.anifriends.domain.shelter;

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

@Entity
@Table(name = "shelter_image")
public class ShelterImage extends BaseTimeEntity {

    @Id
    @Column(name = "shelter_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shelterImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Column(name = "image_url")
    private String imageUrl;

}
