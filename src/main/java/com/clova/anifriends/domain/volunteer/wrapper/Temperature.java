package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Temperature {

    @Column(name = "temperature")
    private Integer temperature;

    protected Temperature() {
    }

    public Temperature(int value) {
        this.temperature = value;
    }

}
