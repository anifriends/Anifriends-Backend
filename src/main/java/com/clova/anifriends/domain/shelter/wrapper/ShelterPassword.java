package com.clova.anifriends.domain.shelter.wrapper;

import static java.util.Objects.isNull;

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
public class ShelterPassword {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 16;

    @Column(name = "password")
    private String password;

    public ShelterPassword(String value) {
        validateShelterPassword(value);
        this.password = value;
    }

    private void validateShelterPassword(String password) {
        if (isNull(password) || password.isBlank()) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST, "비밀번호는 필수 항목입니다.");
        }

        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST,
                "비밀번호는 최소 6자, 최대 16자입니다.");
        }
    }
}
