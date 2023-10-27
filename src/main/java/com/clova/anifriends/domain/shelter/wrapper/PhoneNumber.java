package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PhoneNumber {

    private String phoneNumber;

    protected PhoneNumber() {
    }

    public PhoneNumber(String value) {
        this.phoneNumber = value;
    }
}
