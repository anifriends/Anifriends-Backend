package com.clova.anifriends.domain.animal.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalBreed {

    @Column(name = "breed")
    private String breed;

    public AnimalBreed(String value) {
        this.breed = value;
    }
}
