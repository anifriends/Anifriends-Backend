package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.animal.exception.AnimalBadRequestException;
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
public class AnimalInformation {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 1000;

    @Column(name = "information")
    private String information;

    public AnimalInformation(String value) {
        validateNotNull(value);
        validateLength(value);
        this.information = value;
    }

    public AnimalInformation updateInformation(
        String information
    ) {
        validateNotNull(information);
        validateLength(information);

        return new AnimalInformation(information);
    }

    private void validateNotNull(String value) {
        if (Objects.isNull(value)) {
            throw new AnimalBadRequestException("기타 정보는 필수값입니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new AnimalBadRequestException(
                MessageFormat.format("기타 정보는 {0}자 이상, {1}자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }
}
