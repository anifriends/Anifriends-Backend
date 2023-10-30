package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VolunteerPassword {

    @Column(name = "password")
    private String password;

    protected VolunteerPassword() {
    }

    public VolunteerPassword(String value) {
        this.password = value;
    }

}
