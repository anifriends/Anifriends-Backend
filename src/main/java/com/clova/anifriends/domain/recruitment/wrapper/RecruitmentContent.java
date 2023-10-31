package com.clova.anifriends.domain.recruitment.wrapper;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class RecruitmentContent {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 1000;

    @Column(name = "content")
    private String content;

    protected RecruitmentContent() {
    }

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
            throw new RecruitmentBadRequestException("본문은 1자 이상, 1000자 이하여야 합니다.");
        }
    }

}
