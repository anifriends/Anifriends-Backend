package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Breed {

    @Column(name = "breed")
    private String breed;

    protected Breed() {
    }

    public Breed(String value) {
        this.breed = value;
    }

}
