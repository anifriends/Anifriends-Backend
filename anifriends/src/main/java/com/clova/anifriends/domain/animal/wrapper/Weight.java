package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Weight {

    @Column(name = "weight")
    private Double weight;

    protected Weight() {
    }

    public Weight(double value) {
        this.weight = value;
    }

}
