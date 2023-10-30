package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ShelterPassword {

    @Column(name = "password")
    private String password;

    protected ShelterPassword() {
    }

    public ShelterPassword(String value) {
        this.password = value;
    }

}
