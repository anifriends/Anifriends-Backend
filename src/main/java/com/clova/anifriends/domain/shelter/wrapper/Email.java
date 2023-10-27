package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email {

    @Column(name = "email")
    private String email;

    protected Email() {
    }

    public Email(String value) {
        this.email = value;
    }

}
