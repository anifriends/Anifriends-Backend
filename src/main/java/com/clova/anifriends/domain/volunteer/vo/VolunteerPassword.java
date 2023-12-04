package com.clova.anifriends.domain.volunteer.vo;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
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
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST, "패스워드는 필수값입니다.");
        }
    }

    private void validateVolunteerPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST,
                MessageFormat.format("패스워드는 {0}자 이상, {1}자 이하여야 합니다.",
                    MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
        }
    }

    public VolunteerPassword updatePassword(
        CustomPasswordEncoder passwordEncoder,
        String rawOldPassword,
        String rawNewPassword
    ) {
        checkOldPasswordEquals(rawOldPassword, passwordEncoder);
        checkNewPasswordNotEquals(rawNewPassword, passwordEncoder);

        return new VolunteerPassword(rawNewPassword, passwordEncoder);
    }

    private void checkOldPasswordEquals(
        String rawOldPassword,
        CustomPasswordEncoder passwordEncoder) {
        if (passwordEncoder.noneMatchesPassword(rawOldPassword, password)) {
            throw new VolunteerBadRequestException(ErrorCode.OLD_PASSWORD_NOT_EQUALS_PREVIOUS,
                "비밀번호가 일치하지 않습니다.");
        }
    }
    
    private void checkNewPasswordNotEquals(
        String rawNewPassword,
        CustomPasswordEncoder passwordEncoder) {
        if (passwordEncoder.matchesPassword(rawNewPassword, password)) {
            throw new VolunteerBadRequestException(ErrorCode.NEW_PASSWORD_EQUALS_PREVIOUS,
                "변경하려는 패스워드와 기존 패스워드가 동일합니다.");
        }
    }
}
