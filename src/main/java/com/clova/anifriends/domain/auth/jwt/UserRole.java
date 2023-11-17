package com.clova.anifriends.domain.auth.jwt;

import com.clova.anifriends.domain.common.EnumType;
import java.util.List;

public enum UserRole implements EnumType {
    ROLE_VOLUNTEER(Constants.ROLE_VOLUNTEER, List.of(Constants.ROLE_VOLUNTEER)),
    ROLE_SHELTER(Constants.ROLE_SHELTER, List.of(Constants.ROLE_SHELTER));

    private final String value;
    private final List<String> authorities;

    UserRole(String value, List<String> authorities) {
        this.value = value;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return name();
    }

    public String getValue() {
        return value;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    private static class Constants {

        private static final String ROLE_VOLUNTEER = "ROLE_VOLUNTEER";
        private static final String ROLE_SHELTER = "ROLE_SHELTER";
    }
}
