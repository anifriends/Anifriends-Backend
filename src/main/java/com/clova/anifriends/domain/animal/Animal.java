package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalBreed;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalInformation;
import com.clova.anifriends.domain.animal.wrapper.AnimalName;
import com.clova.anifriends.domain.animal.wrapper.AnimalNeutered;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.animal.wrapper.AnimalWeight;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.shelter.Shelter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "animal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Animal extends BaseTimeEntity {

    private static final int MAX_IMAGES_SIZE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long animalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Embedded
    private AnimalName name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AnimalType type;

    @Embedded
    private AnimalBreed breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private AnimalGender gender;

    @Embedded
    private AnimalNeutered neutered;

    @Enumerated(EnumType.STRING)
    @Column(name = "active")
    private AnimalActive active;

    @Embedded
    private AnimalWeight weight;

    @Embedded
    private AnimalInformation information;

    @Column(name = "is_adopted")
    private boolean isAdopted;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.PERSIST,
        fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AnimalImage> images = new ArrayList<>();

    public Animal(
        Shelter shelter,
        String name,
        LocalDate birthDate,
        String type,
        String breed,
        String gender,
        boolean isNeutered,
        String active,
        double weight,
        String information,
        List<String> imageUrls
    ) {
        validateImageIsNotNull(imageUrls);
        validateImageSize(imageUrls);
        this.shelter = shelter;
        this.name = new AnimalName(name);
        this.birthDate = birthDate;
        this.type = AnimalType.valueOf(type);
        this.breed = new AnimalBreed(breed);
        this.gender = AnimalGender.valueOf(gender);
        this.neutered = new AnimalNeutered(isNeutered);
        this.active = AnimalActive.valueOf(active);
        this.weight = new AnimalWeight(weight);
        this.information = new AnimalInformation(information);
        this.images = imageUrls.stream()
            .map(url -> new AnimalImage(this, url))
            .toList();
        this.isAdopted = false;
    }

    public void updateAnimal(
        String name,
        LocalDate birthDate,
        AnimalType type,
        String breed,
        AnimalGender gender,
        Boolean isNeutered,
        AnimalActive active,
        Double weight,
        String information,
        List<String> imageUrls,
        ImageRemover imageRemover
    ) {
        this.name = this.name.updateName(name);
        this.birthDate = LocalDate.of(
            birthDate.getYear(),
            birthDate.getMonth(),
            birthDate.getDayOfMonth()
        );
        this.type = type;
        this.breed = this.breed.updateBreed(breed);
        this.gender = gender;
        this.neutered = this.neutered.updateIsNeutered(isNeutered);
        this.active = active;
        this.weight = this.weight.updateWeight(weight);
        this.information = this.information.updateInformation(information);
        updateImages(imageUrls, imageRemover);
    }

    private void updateImages(
        List<String> images, ImageRemover imageRemover
    ) {
        if (Objects.nonNull(images)) {
            validateImageSize(images);
            validateImageIsNotNull(images);

            deleteNotContainsImageUrls(images, imageRemover);
            addNewImageUrls(images);
        }
    }

    private void deleteNotContainsImageUrls(
        List<String> updateImageUrls,
        ImageRemover imageRemover
    ) {
        List<String> deleteImageUrls = this.images.stream()
            .map(AnimalImage::getImageUrl)
            .filter(existsImageUrl -> !updateImageUrls.contains(existsImageUrl))
            .toList();
        imageRemover.removeImages(deleteImageUrls);
    }

    private void addNewImageUrls(List<String> updateImageUrls) {
        List<AnimalImage> existsVolunteerImages = filterRemainImages(updateImageUrls);
        List<AnimalImage> newVolunteerImages = filterNewImages(updateImageUrls);

        this.images.clear();
        this.images.addAll(existsVolunteerImages);
        this.images.addAll(newVolunteerImages);
    }

    private List<AnimalImage> filterRemainImages(List<String> updateImageUrls) {
        return this.images.stream()
            .filter(AnimalImage -> updateImageUrls.contains(AnimalImage.getImageUrl()))
            .toList();
    }

    private List<AnimalImage> filterNewImages(
        List<String> updateImageUrls
    ) {
        List<String> existsImageUrls = getImageUrls();

        return updateImageUrls.stream()
            .filter(imageUrl -> !existsImageUrls.contains(imageUrl))
            .map(imageUrl -> new AnimalImage(this, imageUrl))
            .toList();
    }

    private void validateImageIsNotNull(List<String> imageUrls) {
        if (Objects.isNull(imageUrls)) {
            throw new AnimalBadRequestException("보호 동물 이미지는 필수값입니다.");
        }
    }

    private void validateImageSize(List<String> imageUrls) {
        if (imageUrls.isEmpty() || imageUrls.size() > MAX_IMAGES_SIZE) {
            throw new AnimalBadRequestException("보호 동물 이미지 크기는 1장 이상, 5장 이하여야 합니다.");
        }
    }

    public Long getAnimalId() {
        return animalId;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public String getName() {
        return this.name.getName();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public AnimalType getType() {
        return type;
    }

    public String getBreed() {
        return breed.getBreed();
    }

    public AnimalGender getGender() {
        return gender;
    }

    public boolean isNeutered() {
        return this.neutered.getIsNeutered();
    }

    public AnimalActive getActive() {
        return active;
    }

    public double getWeight() {
        return weight.getWeight();
    }

    public String getInformation() {
        return information.getInformation();
    }

    public List<String> getImageUrls() {
        return images.stream()
            .map(AnimalImage::getImageUrl)
            .toList();
    }

    public boolean isAdopted() {
        return isAdopted;
    }
}
