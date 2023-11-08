package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.common.EnumType;

public enum AnimalAge implements EnumType {
    BABY(0, 7),
    JUNIOR(7, 36),
    ADULT(36, 108),
    SENIOR(108, Integer.MAX_VALUE);

    private final int minMonth;
    private final int maxMonth;

    AnimalAge(int minMonth, int maxMonth) {
        this.minMonth = minMonth;
        this.maxMonth = maxMonth;
    }

    public int getMinMonth() {
        return this.minMonth;
    }

    public int getMaxMonth() {
        return this.maxMonth;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
