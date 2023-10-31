package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerPassword {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 16;

    @Column(name = "password")
    private String password;

    protected VolunteerPassword() {
    }

    public VolunteerPassword(String value) {
        validateVolunteerPassword(value);
        this.password = value;
    }

    private void validateVolunteerPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new VolunteerBadRequestException("비밀번호는 최소 6자, 최대 16자입니다.");
        }
    }
}
