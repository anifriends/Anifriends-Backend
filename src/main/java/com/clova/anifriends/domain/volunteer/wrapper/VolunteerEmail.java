package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VolunteerEmail {

    @Column(name = "email")
    private String email;

    protected VolunteerEmail() {
    }

    public VolunteerEmail(String value) {
        this.email = value;
    }

}
