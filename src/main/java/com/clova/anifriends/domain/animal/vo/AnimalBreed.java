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
public class AnimalBreed {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    @Column(name = "breed")
    private String breed;

    public AnimalBreed(String value) {
        validateNotNull(value);
        validateLength(value);
        this.breed = value;
    }

    public AnimalBreed updateBreed(String breed) {
        validateNotNull(breed);
        validateLength(breed);

        return new AnimalBreed(breed);
    }

    private void validateNotNull(String value) {
        if (Objects.isNull(value)) {
            throw new AnimalBadRequestException("품종은 필수값입니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new AnimalBadRequestException(
                MessageFormat.format("품종은 {0}자 이상, {1}자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }
}
