package com.clova.anifriends.domain.animal;

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
@Table(name = "animal_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalImage extends BaseTimeEntity {

    @Id
    @Column(name = "animal_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @Column(name = "image_url")
    private String imageUrl;

    public AnimalImage(Animal animal, String imageUrl) {
        this.animal = animal;
        this.imageUrl = imageUrl;
    }
}
