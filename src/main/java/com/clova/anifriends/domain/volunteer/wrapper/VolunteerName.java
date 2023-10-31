package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VolunteerName {

    private static final int MAX_VOLUNTEER_NAME_LENGTH = 10;

    @Column(name = "name")
    private String name;

    protected VolunteerName() {
    }

    public VolunteerName(String value) {
        validateVolunteerName(value);
        this.name = value;
    }

    private void validateVolunteerName(String name) {
        if (name == null || name.isBlank()) {
            throw new VolunteerBadRequestException("이름은 필수 항목입니다.");
        }

        if (name.length() > MAX_VOLUNTEER_NAME_LENGTH) {
            throw new VolunteerBadRequestException("이름은 최대 10자입니다.");
        }
    }
}
