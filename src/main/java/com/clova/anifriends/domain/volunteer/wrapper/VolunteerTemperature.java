package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerTemperature {

    private static final int MAX_VOLUNTEER_TEMPERATURE = 99;

    @Column(name = "temperature")
    private Integer temperature;

    protected VolunteerTemperature() {
    }

    public VolunteerTemperature(int value) {
        validateVolunteerTemperature(value);
        this.temperature = value;
    }

    private void validateVolunteerTemperature(int temperature) {
        if (temperature > MAX_VOLUNTEER_TEMPERATURE) {
            throw new VolunteerBadRequestException("봉사자 체온은 99도 이하입니다.");
        }
    }
}
