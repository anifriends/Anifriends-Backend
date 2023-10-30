package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VolunteerTemperature {

    @Column(name = "temperature")
    private Integer temperature;

    protected VolunteerTemperature() {
    }

    public VolunteerTemperature(int value) {
        this.temperature = value;
    }

}
