package com.clova.anifriends.docs.enumtype;

import com.clova.anifriends.domain.common.EnumType;

public enum TestEnum implements EnumType {
    MALE("남성"), FEMALE("여성");

    private final String value;

    TestEnum(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
