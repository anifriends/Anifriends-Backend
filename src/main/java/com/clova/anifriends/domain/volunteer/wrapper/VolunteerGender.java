package com.clova.anifriends.domain.volunteer.wrapper;

import com.clova.anifriends.EnumType;

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
}
