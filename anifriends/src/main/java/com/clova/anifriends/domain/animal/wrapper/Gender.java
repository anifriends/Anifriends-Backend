package com.clova.anifriends.domain.animal.wrapper;

import com.clova.anifriends.EnumType;

public enum Gender implements EnumType {

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
