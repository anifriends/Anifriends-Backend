package com.clova.anifriends.domain.shelter;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.shelter.wrapper.ShelterEmail;
import com.clova.anifriends.domain.shelter.wrapper.ShelterName;
import com.clova.anifriends.domain.shelter.wrapper.ShelterPassword;
import com.clova.anifriends.domain.shelter.wrapper.ShelterAddressInfo;
import com.clova.anifriends.domain.shelter.wrapper.ShelterPhoneNumberInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shelter")
public class Shelter extends BaseTimeEntity {

    @Id
    @Column(name = "shelter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shelterId;

    @Embedded
    private ShelterEmail email;

    @Embedded
    private ShelterPassword password;

    @Embedded
    private ShelterName name;

    @Embedded
    private ShelterPhoneNumberInfo phoneNumberInfo;

    @Embedded
    private ShelterAddressInfo addressInfo;

    protected Shelter() {
    }

    public Shelter(
        String email,
        String password,
        String address,
        String addressDetail,
        String name,
        String phoneNumber,
        String sparePhoneNumber,
        boolean isOpenedAddress
    ) {
        this.email = new ShelterEmail(email);
        this.password = new ShelterPassword(password);
        this.name = new ShelterName(name);
        this.phoneNumberInfo = new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber);
        this.addressInfo = new ShelterAddressInfo(address, addressDetail, isOpenedAddress);
    }
}
