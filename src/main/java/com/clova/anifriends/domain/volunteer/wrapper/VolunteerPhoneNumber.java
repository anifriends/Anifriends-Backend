package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerPhoneNumber {

    private static final int MIN_PHONE_NUMBER_LENGTH = 9;
    private static final int MAX_PHONE_NUMBER_LENGTH = 11;

    @Column(name = "phone_number")
    private String phoneNumber;

    protected VolunteerPhoneNumber() {
    }

    public VolunteerPhoneNumber(String value) {
        validateVolunteerPhoneNumber(value);
        this.phoneNumber = value;
    }

    private void validateVolunteerPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < MIN_PHONE_NUMBER_LENGTH
            || phoneNumber.length() > MAX_PHONE_NUMBER_LENGTH) {
            throw new VolunteerBadRequestException("전화번호는 최소 9자, 최대 11자입니다.");
        }
    }
}
