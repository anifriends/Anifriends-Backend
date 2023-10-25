package com.clova.anifriends.docs;

import com.clova.anifriends.EnumType;

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

    @Override
    public String getValue() {
        return value;
    }
}
