package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Information {

    @Column(name = "information")
    private String information;

    protected Information() {
    }

    public Information(String value) {
        this.information = value;
    }
}
