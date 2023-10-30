package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AnimalBreed {

    @Column(name = "breed")
    private String breed;

    protected AnimalBreed() {
    }

    public AnimalBreed(String value) {
        this.breed = value;
    }

}
