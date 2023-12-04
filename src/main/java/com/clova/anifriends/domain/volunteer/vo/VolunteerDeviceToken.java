package com.clova.anifriends.domain.volunteer.vo;

import static java.util.Objects.isNull;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerDeviceToken {

    @Column(name = "device_token")
    private String deviceToken;

    public VolunteerDeviceToken(String value) {
        validateDeviceToken(value);
        this.deviceToken = value;
    }

    private void validateDeviceToken(String deviceToken) {
        if (isNull(deviceToken) || deviceToken.isBlank()) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST, "deviceToken은 필수 입력 항목입니다.");
        }
    }
}
