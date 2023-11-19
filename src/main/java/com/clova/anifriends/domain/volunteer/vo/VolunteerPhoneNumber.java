package com.clova.anifriends.domain.volunteer.vo;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerPhoneNumber {

    private static final String PHONE_NUMBER_REGEX = "^(\\d{2,3}-\\d{3,4}-\\d{4})$";

    @Column(name = "phone_number")
    private String phoneNumber;

    protected VolunteerPhoneNumber() {
    }

    public VolunteerPhoneNumber(String value) {
        validateVolunteerPhoneNumber(value);
        this.phoneNumber = value;
    }

    private void validateVolunteerPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST,
                "전화번호 형식이 올바르지 않습니다.");
        }
    }

    public VolunteerPhoneNumber updatePhoneNumber(String phoneNumber) {
        return phoneNumber != null ? new VolunteerPhoneNumber(phoneNumber) : this;
    }
}
