package com.clova.anifriends.domain.shelter;

import static java.util.Objects.nonNull;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.vo.ShelterAddressInfo;
import com.clova.anifriends.domain.shelter.vo.ShelterDeviceToken;
import com.clova.anifriends.domain.shelter.vo.ShelterEmail;
import com.clova.anifriends.domain.shelter.vo.ShelterName;
import com.clova.anifriends.domain.shelter.vo.ShelterPassword;
import com.clova.anifriends.domain.shelter.vo.ShelterPhoneNumberInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shelter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shelter extends BaseTimeEntity {

    private static final String BLANK = "";

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

    @Embedded
    private ShelterDeviceToken deviceToken;

    @OneToOne(mappedBy = "shelter", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void updateShelter(
        String name,
        String imageUrl,
        String address,
        String addressDetail,
        String phoneNumber,
        String sparePhoneNumber,
        Boolean isOpenedAddress
    ) {
        this.name = this.name.update(name);
        this.image = updateImage(imageUrl);
        this.addressInfo = this.addressInfo.update(address, addressDetail, isOpenedAddress);
        this.phoneNumberInfo = this.phoneNumberInfo.update(phoneNumber, sparePhoneNumber);
    }

    private ShelterImage updateImage(String imageUrl) {
        if(nonNull(imageUrl)) {
            if(imageUrl.isBlank()) {
                return null;
            }
            if(nonNull(image) && image.isSameWith(imageUrl)) {
                return image;
            }
            return new ShelterImage(this, imageUrl);
        }
        return image;
    }

    public Optional<String> findImageToDelete(String newImageUrl) {
        if (nonNull(this.image) && this.image.isDifferentFrom(newImageUrl)) {
            return Optional.of(this.image.getImageUrl());
        }
        return Optional.empty();
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

    public String getImage() {
        return this.image == null ? BLANK : this.image.getImageUrl();
    }

    public String getDeviceToken() {
        return this.deviceToken == null ? null : this.deviceToken.getDeviceToken();
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = new ShelterDeviceToken(deviceToken);
    }
}
