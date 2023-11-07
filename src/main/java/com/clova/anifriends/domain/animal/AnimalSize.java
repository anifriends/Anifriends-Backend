package com.clova.anifriends.domain.animal;

import com.clova.anifriends.domain.common.EnumType;

public enum AnimalSize implements EnumType {
    SMALL(0, 8),
    MEDIUM(8, 18),
    LARGE(18, Integer.MAX_VALUE),
    ;

    private final int minWeight;
    private final int maxWeight;

    AnimalSize(int minWeight, int maxWeight) {
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public int getMinWeight() {
        return this.minWeight;
    }

    public int getMaxWeight() {
        return this.maxWeight;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
