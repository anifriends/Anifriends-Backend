package com.clova.anifriends.domain.shelter.wrapper;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterName {

    private static final int MAX_SHELTER_NAME_LENGTH = 10;

    @Column(name = "name")
    private String name;

    public ShelterName(String value) {
        validateName(value);
        this.name = value;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST, "이름은 필수 항목입니다.");
        }

        if (name.length() > MAX_SHELTER_NAME_LENGTH) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST, "이름은 최대 10자입니다.");
        }
    }
}
