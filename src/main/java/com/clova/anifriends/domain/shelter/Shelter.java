package com.clova.anifriends.domain.shelter;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.wrapper.ShelterAddressInfo;
import com.clova.anifriends.domain.shelter.wrapper.ShelterEmail;
import com.clova.anifriends.domain.shelter.wrapper.ShelterName;
import com.clova.anifriends.domain.shelter.wrapper.ShelterPassword;
import com.clova.anifriends.domain.shelter.wrapper.ShelterPhoneNumberInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shelter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToOne(mappedBy = "shelter")
    private ShelterImage image;

    public Shelter(
        String email,
        String password,
        String address,
        String addressDetail,
        String name,
        String phoneNumber,
        String sparePhoneNumber,
        boolean isOpenedAddress,
        CustomPasswordEncoder passwordEncoder
    ) {
        this.email = new ShelterEmail(email);
        this.password = new ShelterPassword(password, passwordEncoder);
        this.name = new ShelterName(name);
        this.phoneNumberInfo = new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber);
        this.addressInfo = new ShelterAddressInfo(address, addressDetail, isOpenedAddress);
    }

    public void updatePassword(
        CustomPasswordEncoder passwordEncoder,
        String rawOldPassword,
        String rawNewPassword) {
        password = password.updatePassword(passwordEncoder, rawOldPassword, rawNewPassword);
    }

    public void updateAddressStatus(
        Boolean updatedAddressStatus
    ) {
        addressInfo = addressInfo.updateAddressStatus(updatedAddressStatus);
    }

    public Long getShelterId() {
        return shelterId;
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public String getName() {
        return name.getName();
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public String getAddress() {
        return this.addressInfo.getAddress();
    }

    public String getAddressDetail() {
        return this.addressInfo.getAddressDetail();
    }

    public Boolean isOpenedAddress() {
        return this.addressInfo.isOpenedAddress();
    }

    public String getPhoneNumber() {
        return this.phoneNumberInfo.getPhoneNumber();
    }

    public String getSparePhoneNumber() {
        return this.phoneNumberInfo.getSparePhoneNumber();
    }

    public String getShelterImageUrl() {
        return this.image == null ? null : this.image.getImageUrl();
    }

    public void updateShelterImage(ShelterImage shelterImage) {
        this.image = shelterImage;
    }

    public String getImage() {
        return this.image == null ? null : this.image.getImageUrl();
    }
}
