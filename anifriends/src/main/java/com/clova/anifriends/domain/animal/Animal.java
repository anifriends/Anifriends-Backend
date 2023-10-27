package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.animal.wrapper.Active;
import com.clova.anifriends.domain.animal.wrapper.Breed;
import com.clova.anifriends.domain.animal.wrapper.Gender;
import com.clova.anifriends.domain.animal.wrapper.Information;
import com.clova.anifriends.domain.animal.wrapper.IsNeutered;
import com.clova.anifriends.domain.animal.wrapper.Name;
import com.clova.anifriends.domain.animal.wrapper.Type;
import com.clova.anifriends.domain.animal.wrapper.Weight;
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
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long animalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Embedded
    private Name name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Embedded
    private Breed breed;

    @Embedded
    private Gender gender;

    @Embedded
    private IsNeutered isNeutered;

    @Enumerated(EnumType.STRING)
    @Column(name = "active")
    private Active active;

    @Embedded
    private Weight weight;

    @Embedded
    private Information information;

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
        this.name = new Name(name);
        this.birthDate = birthDate;
        this.type = Type.valueOf(type);
        this.breed = new Breed(breed);
        this.gender = Gender.valueOf(gender);
        this.isNeutered = new IsNeutered(isNeutered);
        this.active = Active.valueOf(active);
        this.weight = new Weight(weight);
        this.information = new Information(information);
    }
}
