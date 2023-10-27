package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class IsClosed {

    @Column(name = "is_closed")
    private Boolean isClosed;

    protected IsClosed() {
    }

    public IsClosed(boolean value) {
        this.isClosed = value;
    }

}
