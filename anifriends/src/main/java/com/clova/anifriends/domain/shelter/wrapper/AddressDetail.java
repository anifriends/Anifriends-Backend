package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AddressDetail {

    @Column(name = "address_detail")
    private String addressDetail;

    protected AddressDetail() {
    }

    public AddressDetail(String value) {
        this.addressDetail = value;
    }

}
