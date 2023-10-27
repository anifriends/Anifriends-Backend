package com.clova.anifriends.domain.shelter.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(name = "address")
    private String address;

    protected Address() {
    }

    public Address(String value) {
        this.address = value;
    }

}
