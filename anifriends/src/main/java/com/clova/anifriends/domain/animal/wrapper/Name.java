package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name")
    private String name;

    protected Name() {
    }

    public Name(String value) {
        this.name = value;
    }

}
