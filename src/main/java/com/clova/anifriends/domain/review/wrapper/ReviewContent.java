package com.clova.anifriends.domain.review.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ReviewContent {

    @Column(name = "content")
    private String content;

    protected ReviewContent() {
    }

    public ReviewContent(String value) {
        this.content = value;
    }

}
