package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AnimalWeight {

    @Column(name = "weight")
    private Double weight;

    protected AnimalWeight() {
    }

    public AnimalWeight(double value) {
        this.weight = value;
    }

}
