package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.common.EnumType;

public enum AnimalAge implements EnumType {
    BABY(0, 6),
    JUNIOR(7, 35),
    ADULT(36, 83),
    SENIOR(84, Integer.MAX_VALUE);

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
