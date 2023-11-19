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
public class RecruitmentContent {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 1000;

    @Column(name = "content")
    private String content;

    public RecruitmentContent(String value) {
        validateNotNull(value);
        validateLength(value);
        this.content = value;
    }

    private void validateNotNull(String value) {
        if(Objects.isNull(value)) {
            throw new RecruitmentBadRequestException("본문은 필수입니다.");
        }
    }

    private void validateLength(String value) {
        if(value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new RecruitmentBadRequestException(
                MessageFormat.format("본문은 {0}자 이상, {1}자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    public RecruitmentContent updateContent(String content) {
        return content != null ? new RecruitmentContent(content) : this;
    }
}
