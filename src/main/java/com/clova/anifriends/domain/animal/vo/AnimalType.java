package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.common.EnumType;

public enum AnimalType implements EnumType {

    DOG,
    CAT,
    ETC,
    ;

    @Override
    public String getName() {
        return this.name();
    }
}
