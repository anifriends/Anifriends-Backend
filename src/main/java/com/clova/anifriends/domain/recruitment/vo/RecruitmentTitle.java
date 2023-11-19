package com.clova.anifriends.domain.recruitment.vo;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentTitle {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    @Column(name = "title")
    private String title;

    public RecruitmentTitle(String value) {
        validateNotNull(value);
        validateLength(value);
        this.title = value;
    }

    private void validateLength(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new RecruitmentBadRequestException(
                MessageFormat.format("제목은 {0}자 이상, {1}자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    private void validateNotNull(String value) {
        if (Objects.isNull(value)) {
            throw new RecruitmentBadRequestException("제목은 필수입니다.");
        }
    }

    public RecruitmentTitle updateTitle(String title) {
        return title != null ? new RecruitmentTitle(title) : this;
    }
}
