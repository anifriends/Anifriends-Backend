package com.clova.anifriends.domain.review.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewContent {

    @Column(name = "content")
    private String content;

    public ReviewContent(String value) {
        this.content = value;
    }
}
