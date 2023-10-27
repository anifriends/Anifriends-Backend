package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Capacity {

    @Column(name = "capacity")
    private Integer capacity;

    protected Capacity() {
    }

    public Capacity(int value) {
        this.capacity = value;
    }

}
