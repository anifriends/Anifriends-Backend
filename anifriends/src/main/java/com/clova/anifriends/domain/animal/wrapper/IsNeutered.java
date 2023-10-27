package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class IsNeutered {

    @Column(name = "isNeutered")
    private Boolean isNeutered;

    protected IsNeutered() {
    }

    public IsNeutered(boolean value) {
        this.isNeutered = value;
    }

}
