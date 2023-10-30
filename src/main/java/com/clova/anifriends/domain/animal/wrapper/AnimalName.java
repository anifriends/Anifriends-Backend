package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class AnimalName {

    @Column(name = "name")
    private String name;

    protected AnimalName() {
    }

    public AnimalName(String value) {
        this.name = value;
    }

}
