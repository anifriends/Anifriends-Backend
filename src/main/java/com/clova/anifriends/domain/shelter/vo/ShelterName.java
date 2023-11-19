package com.clova.anifriends.domain.shelter.vo;

import static java.util.Objects.isNull;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterName {

    private static final int MAX_SHELTER_NAME_LENGTH = 20;

    @Column(name = "name")
    private String name;

    public ShelterName(String value) {
        validateName(value);
        this.name = value;
    }

    public ShelterName update(String value) {
        return new ShelterName(value);
    }

    private void validateName(String name) {
        if (isNull(name) || name.isBlank()) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST, "이름은 필수 항목입니다.");
        }

        if (name.length() > MAX_SHELTER_NAME_LENGTH) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST,
                MessageFormat.format("이름은 최대 {0}입니다.", MAX_SHELTER_NAME_LENGTH));
        }
    }
}
