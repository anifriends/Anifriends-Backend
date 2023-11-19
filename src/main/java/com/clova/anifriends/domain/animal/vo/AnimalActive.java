package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum AnimalActive implements EnumType {

    QUIET,
    NORMAL,
    ACTIVE,
    VERY_ACTIVE,
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
