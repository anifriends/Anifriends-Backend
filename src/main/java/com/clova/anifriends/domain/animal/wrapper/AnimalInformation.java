package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AnimalInformation {

    @Column(name = "information")
    private String information;

    protected AnimalInformation() {
    }

    public AnimalInformation(String value) {
        this.information = value;
    }
}
