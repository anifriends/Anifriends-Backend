package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.animal.wrapper.AnimalActive;
import com.clova.anifriends.domain.animal.wrapper.AnimalBreed;
import com.clova.anifriends.domain.animal.wrapper.AnimalGender;
import com.clova.anifriends.domain.animal.wrapper.AnimalInformation;
import com.clova.anifriends.domain.animal.wrapper.AnimalName;
import com.clova.anifriends.domain.animal.wrapper.AnimalNeutered;
import com.clova.anifriends.domain.animal.wrapper.AnimalType;
import com.clova.anifriends.domain.animal.wrapper.AnimalWeight;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.shelter.Shelter;
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
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "animal")
public class Animal extends BaseTimeEntity {

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

    @Embedded
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

    protected Animal() {
    }

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
        String information
    ) {
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
}
