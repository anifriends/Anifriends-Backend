package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerName {

    @Column(name = "name")
    private String name;

    protected VolunteerName() {
    }

    public VolunteerName(String value) {
        this.name = value;
    }

}
