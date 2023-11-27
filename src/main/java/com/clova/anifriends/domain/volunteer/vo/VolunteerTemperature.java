package com.clova.anifriends.domain.volunteer.vo;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class VolunteerTemperature {

    private static final int MAX_VOLUNTEER_TEMPERATURE = 99;

    @Column(name = "temperature")
    private Integer temperature;

    public VolunteerTemperature(int value) {
        validateVolunteerTemperature(value);
        this.temperature = value;
    }

    private void validateVolunteerTemperature(int temperature) {
        if (temperature > MAX_VOLUNTEER_TEMPERATURE) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST,
                MessageFormat.format("봉사자 체온은 {0}도 이하입니다.", MAX_VOLUNTEER_TEMPERATURE)
            );
        }
    }

    public VolunteerTemperature increase(int temperature) {
        return new VolunteerTemperature(
            Math.min(this.temperature + temperature, MAX_VOLUNTEER_TEMPERATURE)
        );
    }
}
