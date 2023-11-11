package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
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
public class VolunteerPassword {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 16;

    @Column(name = "password")
    private String password;

    public VolunteerPassword(String rawPassword, CustomPasswordEncoder passwordEncoder) {
        validateNotNull(rawPassword);
        validateVolunteerPasswordLength(rawPassword);
        this.password = passwordEncoder.encodePassword(rawPassword);
    }

    private void validateNotNull(String password) {
        if (Objects.isNull(password)) {
            throw new ShelterBadRequestException("패스워드는 필수값입니다.");
        }
    }

    private void validateVolunteerPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST,
                MessageFormat.format("패스워드는 {0}자 이상, {1}자 이하여야 합니다.",
                    MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
        }
    }
}
