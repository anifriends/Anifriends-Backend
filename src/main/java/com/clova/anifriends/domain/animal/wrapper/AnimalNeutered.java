package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class AnimalNeutered {

    @Column(name = "is_neutered")
    private Boolean isNeutered;

    protected AnimalNeutered() {
    }

    public AnimalNeutered(boolean value) {
        this.isNeutered = value;
    }
}
