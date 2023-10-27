package com.clova.anifriends.domain.animal;

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
@Table(name = "animal_image")
public class AnimalImage {

    @Id
    @Column(name = "animal_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @Column(name = "image_url")
    private String imageUrl;
}
