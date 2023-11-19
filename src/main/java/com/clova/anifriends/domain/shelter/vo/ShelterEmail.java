package com.clova.anifriends.domain.shelter.vo;

import static java.util.Objects.isNull;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterEmail {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@([a-zA-Z0-9.-]{1,255})$");

    @Column(name = "email")
    private String email;

    public ShelterEmail(String value) {
        validateEmail(value);
        this.email = value;
    }

    private void validateEmail(String email) {
        if (isNull(email) || email.isBlank()) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST, "이메일은 필수 항목입니다.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ShelterBadRequestException(ErrorCode.BAD_REQUEST, "잘못된 이메일 입력값입니다.");
        }
    }
}
