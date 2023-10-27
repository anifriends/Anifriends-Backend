package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class IsOpenedAddress {

    @Column(name = "is_opened_address")
    private Boolean isOpenedAddress;

    protected IsOpenedAddress() {
    }

    public IsOpenedAddress(boolean value) {
        this.isOpenedAddress = value;
    }

}
