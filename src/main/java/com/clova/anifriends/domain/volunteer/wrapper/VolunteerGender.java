package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.EnumType;
import com.clova.anifriends.domain.volunteer.exception.NotFoundVolunteerGenderException;
import java.util.Arrays;

public enum VolunteerGender implements EnumType {

    MALE,
    FEMALE
    ;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.name();
    }

    public static VolunteerGender from(String gender) {
        return Arrays.stream(VolunteerGender.values())
            .filter(volunteerGender -> volunteerGender.name().equalsIgnoreCase(gender))
            .findAny()
            .orElseThrow(() -> new NotFoundVolunteerGenderException("존재하지 않는 성별입니다."));
    }
}
