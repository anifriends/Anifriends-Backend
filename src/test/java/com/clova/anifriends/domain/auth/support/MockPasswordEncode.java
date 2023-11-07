package com.clova.anifriends.domain.auth.support;

import org.springframework.security.crypto.password.PasswordEncoder;

public class MockPasswordEncode implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return new StringBuilder(rawPassword).reverse().toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
