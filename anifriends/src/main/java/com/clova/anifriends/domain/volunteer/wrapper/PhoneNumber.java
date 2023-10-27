package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PhoneNumber {

    @Column(name = "phone_number")
    private String phoneNumber;

    protected PhoneNumber() {
    }

    public PhoneNumber(String value) {
        this.phoneNumber = value;
    }
}
