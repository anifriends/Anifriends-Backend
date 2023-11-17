package com.clova.anifriends.domain.auth.support;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;

public class MockPasswordEncoder implements CustomPasswordEncoder {

    @Override
    public String encodePassword(String rawPassword) {
        return new StringBuilder(rawPassword).reverse().toString();
    }

    @Override
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return encodePassword(rawPassword).equals(encodedPassword);
    }

    @Override
    public boolean noneMatchesPassword(String rawPassword, String encodedPassword) {
        return !matchesPassword(rawPassword, encodedPassword);
    }
}
