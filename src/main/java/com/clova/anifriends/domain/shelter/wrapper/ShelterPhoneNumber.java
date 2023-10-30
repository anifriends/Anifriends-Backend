package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShelterPhoneNumber {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "spare_phone_number")
    private String sparePhoneNumber;

    protected ShelterPhoneNumber() {
    }

    public ShelterPhoneNumber(String phoneNumber, String sparePhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.sparePhoneNumber = sparePhoneNumber;
    }
}
