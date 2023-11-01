package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterPhoneNumberInfo {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "spare_phone_number")
    private String sparePhoneNumber;

    public ShelterPhoneNumberInfo(String phoneNumber, String sparePhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.sparePhoneNumber = sparePhoneNumber;
    }
}
