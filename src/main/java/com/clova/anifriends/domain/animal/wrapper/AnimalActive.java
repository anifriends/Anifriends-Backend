package com.clova.anifriends.domain.animal.wrapper;

public enum AnimalActive {

    QUIET,
    NORMAL,
    ACTIVE,
    VERY_ACTIVE,
    ;

    public String getName() {
        return this.name();
    }
}
