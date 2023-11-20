package com.clova.anifriends.domain.animal.vo;

import com.clova.anifriends.domain.common.EnumType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnimalNeuteredFilter implements EnumType {

    IS_NEUTERED(true),
    IS_NOT_NEUTERED(false),
    ;

    private final boolean isNeutered;

    public boolean isNeutered() {
        return isNeutered;
    }

    @Override
    public String getName() {
        return name();
    }
}
