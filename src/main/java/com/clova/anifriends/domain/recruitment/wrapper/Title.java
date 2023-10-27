package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Title {

    @Column(name = "title")
    private String title;

    protected Title() {
    }

    public Title(String value) {
        this.title = value;
    }

}
