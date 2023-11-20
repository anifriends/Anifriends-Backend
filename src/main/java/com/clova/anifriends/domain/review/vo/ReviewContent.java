package com.clova.anifriends.domain.review.vo;

import com.clova.anifriends.domain.review.exception.ReviewBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewContent {

    private static final int CONTENT_MIN_LENGTH = 10;
    private static final int CONTENT_MAX_LENGTH = 300;

    @Column(name = "content")
    private String content;

    public ReviewContent(String value) {
        validateLength(value);
        this.content = value;
    }

    private void validateLength(String value) {
        if (CONTENT_MIN_LENGTH > value.length() || value.length() > CONTENT_MAX_LENGTH) {
            throw new ReviewBadRequestException(
                MessageFormat.format(
                    "리뷰 내용은 {0}자 이상 {1}자 이하로 작성해야 합니다.",
                    CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH
                )
            );
        }
    }

    public ReviewContent updateContent(String value) {
        return value != null ? new ReviewContent(value) : this;
    }
}
