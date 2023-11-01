package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalName {

    @Column(name = "name")
    private String name;

    public AnimalName(String value) {
        this.name = value;
    }
}
