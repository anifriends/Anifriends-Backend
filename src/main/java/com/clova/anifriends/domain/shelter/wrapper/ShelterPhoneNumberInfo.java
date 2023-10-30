package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ShelterPhoneNumberInfo {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "spare_phone_number")
    private String sparePhoneNumber;

    protected ShelterPhoneNumberInfo() {
    }

    public ShelterPhoneNumberInfo(String phoneNumber, String sparePhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.sparePhoneNumber = sparePhoneNumber;
    }
}
