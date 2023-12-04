package com.clova.anifriends.domain.volunteer.vo;

import com.clova.anifriends.domain.common.EnumType;
import com.clova.anifriends.domain.volunteer.exception.NotFoundVolunteerGenderException;
import com.clova.anifriends.global.exception.ErrorCode;
import java.util.Arrays;

public enum VolunteerGender implements EnumType {

    MALE,
    FEMALE;

    @Override
    public String getName() {
        return this.name();
    }

    public static VolunteerGender from(String gender) {
        return Arrays.stream(VolunteerGender.values())
            .filter(volunteerGender -> volunteerGender.name().equalsIgnoreCase(gender))
            .findAny()
            .orElseThrow(() -> new NotFoundVolunteerGenderException(ErrorCode.BAD_REQUEST,
                "존재하지 않는 성별입니다."));
    }
}
