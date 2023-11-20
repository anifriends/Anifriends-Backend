package com.clova.anifriends.domain.volunteer.vo;

import com.clova.anifriends.domain.volunteer.exception.VolunteerBadRequestException;
import com.clova.anifriends.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerName {

    private static final int MAX_VOLUNTEER_NAME_LENGTH = 10;

    @Column(name = "name")
    private String name;

    public VolunteerName(String value) {
        validateNotNull(value);
        validateVolunteerNameLength(value);
        this.name = value;
    }

    private void validateNotNull(String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST, "이름은 필수 항목입니다.");
        }
    }

    private void validateVolunteerNameLength(String name) {
        if (name.length() > MAX_VOLUNTEER_NAME_LENGTH) {
            throw new VolunteerBadRequestException(ErrorCode.BAD_REQUEST, "이름은 최대 10자입니다.");
        }
    }

    public VolunteerName updateName(String name) {
        return name != null ? new VolunteerName(name) : this;
    }
}
