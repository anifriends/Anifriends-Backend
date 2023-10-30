package com.clova.anifriends.domain.shelter;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.shelter.wrapper.Email;
import com.clova.anifriends.domain.shelter.wrapper.Name;
import com.clova.anifriends.domain.shelter.wrapper.Password;
import com.clova.anifriends.domain.shelter.wrapper.ShelterAddressInfo;
import com.clova.anifriends.domain.shelter.wrapper.ShelterPhoneNumber;
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
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private ShelterPhoneNumber shelterPhoneNumber;

    @Embedded
    private ShelterAddressInfo shelterAddressInfo;

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
        this.email = new Email(email);
        this.password = new Password(password);
        this.name = new Name(name);
        this.shelterPhoneNumber = new ShelterPhoneNumber(phoneNumber, sparePhoneNumber);
        this.shelterAddressInfo = new ShelterAddressInfo(address, addressDetail, isOpenedAddress);
    }
}
