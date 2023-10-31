package com.clova.anifriends.domain.recruitment.wrapper;

import com.clova.anifriends.domain.recruitment.exception.RecruitmentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class RecruitmentTitle {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    @Column(name = "title")
    private String title;

    protected RecruitmentTitle() {
    }

    public RecruitmentTitle(String value) {
        validateNotNull(value);
        validateLength(value);
        this.title = value;
    }

    private void validateLength(String value) {
        if(value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new RecruitmentBadRequestException("제목은 1자 이상, 100자 이하여야 합니다.");
        }
    }

    private void validateNotNull(String value) {
        if(Objects.isNull(value)) {
            throw new RecruitmentBadRequestException("제목은 필수입니다.");
        }
    }

}
