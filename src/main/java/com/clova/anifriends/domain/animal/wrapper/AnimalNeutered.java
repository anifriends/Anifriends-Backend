package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalNeutered {

    @Column(name = "is_neutered", nullable = false)
    private Boolean isNeutered;

    public AnimalNeutered(boolean value) {
        this.isNeutered = value;
    }
}
