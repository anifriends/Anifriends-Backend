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
public class AnimalName {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    @Column(name = "name")
    private String name;

    public AnimalName(String value) {
        validateNotNull(value);
        validateLength(value);
        this.name = value;
    }

    public AnimalName updateName(String name) {
        validateNotNull(name);
        validateLength(name);

        return new AnimalName(name);
    }

    private void validateLength(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new AnimalBadRequestException(
                MessageFormat.format("보호 동물 이름은 {0}자 이상, {1}자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    private void validateNotNull(String value) {
        if (Objects.isNull(value)) {
            throw new AnimalBadRequestException("보호 동물 이름은 필수값입니다.");
        }
    }
}
