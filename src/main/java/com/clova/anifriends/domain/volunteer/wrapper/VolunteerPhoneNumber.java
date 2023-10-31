package com.clova.anifriends.domain.volunteer.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerPhoneNumber {

    @Column(name = "phone_number")
    private String phoneNumber;

    protected VolunteerPhoneNumber() {
    }

    public VolunteerPhoneNumber(String value) {
        this.phoneNumber = value;
    }
}
