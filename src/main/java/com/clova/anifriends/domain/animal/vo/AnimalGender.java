package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum AnimalGender implements EnumType {

    MALE,
    FEMALE
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
