package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ShelterAddressInfo {

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "is_opened_address")
    private boolean isOpenedAddress;

    protected ShelterAddressInfo() {
    }

    public ShelterAddressInfo(String address, String addressDetail, boolean isOpenedAddress) {
        this.address = address;
        this.addressDetail = addressDetail;
        this.isOpenedAddress = isOpenedAddress;
    }
}
