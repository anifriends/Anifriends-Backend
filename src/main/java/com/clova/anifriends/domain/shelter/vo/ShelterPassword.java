package com.clova.anifriends.domain.shelter.vo;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
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
public class ShelterPassword {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 16;

    @Column(name = "password")
    private String password;

    public ShelterPassword(String rawPassword, CustomPasswordEncoder passwordEncoder) {
        validateNotNull(rawPassword);
        validatePasswordLength(rawPassword);
        this.password = passwordEncoder.encodePassword(rawPassword);
    }

    private void validateNotNull(String password) {
        if (Objects.isNull(password)) {
            throw new ShelterBadRequestException("패스워드는 필수값입니다.");
        }
    }

    private void validatePasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ShelterBadRequestException(
                MessageFormat.format("패스워드는 {0}자 이상, {1}자 이하여야 합니다.",
                    MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH));
        }
    }

    public ShelterPassword updatePassword(
        CustomPasswordEncoder passwordEncoder,
        String rawOldPassword,
        String rawNewPassword) {
        checkOldPasswordEquals(rawOldPassword, passwordEncoder);
        checkNewPasswordNotEquals(rawNewPassword, passwordEncoder);
        return new ShelterPassword(rawNewPassword, passwordEncoder);
    }

    private void checkOldPasswordEquals(
        String rawOldPassword,
        CustomPasswordEncoder passwordEncoder) {
        if (passwordEncoder.noneMatchesPassword(rawOldPassword, password)) {
            throw new ShelterBadRequestException(ErrorCode.OLD_PASSWORD_NOT_EQUALS_PREVIOUS,
                "비밀번호가 일치하지 않습니다.");
        }
    }

    private void checkNewPasswordNotEquals(
        String rawNewPassword,
        CustomPasswordEncoder passwordEncoder) {
        if (passwordEncoder.matchesPassword(rawNewPassword, password)) {
            throw new ShelterBadRequestException(ErrorCode.NEW_PASSWORD_EQUALS_PREVIOUS,
                "변경하려는 패스워드와 기존 패스워드가 동일합니다.");
        }
    }
}
