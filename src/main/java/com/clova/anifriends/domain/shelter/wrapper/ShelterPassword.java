package com.clova.anifriends.domain.shelter.wrapper;

import static java.util.Objects.isNull;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterPassword {

    @Column(name = "password")
    private String password;

    public ShelterPassword(String value) {
        validateNotNull(value);
        validateNotBlank(value);
        this.password = value;
    }

    private void validateNotNull(String password) {
        if (isNull(password)) {
            throw new ShelterBadRequestException("비밀번호는 필수 항목입니다.");
        }
    }

    private void validateNotBlank(String password) {
        if (password.isBlank()) {
            throw new ShelterBadRequestException("비밀번호는 필수 항목입니다.");
        }
    }
}
