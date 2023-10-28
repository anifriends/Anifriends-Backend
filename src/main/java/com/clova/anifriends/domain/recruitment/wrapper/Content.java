package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Content {

    @Column(name = "content")
    private String content;

    protected Content() {
    }

    public Content(String value) {
        this.content = value;
    }

}
