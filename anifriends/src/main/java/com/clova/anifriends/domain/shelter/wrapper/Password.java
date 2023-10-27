package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(name = "password")
    private String password;

    protected Password() {
    }

    public Password(String value) {
        this.password = value;
    }

}
