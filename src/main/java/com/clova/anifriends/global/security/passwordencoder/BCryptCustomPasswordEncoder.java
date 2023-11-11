package com.clova.anifriends.global.security.passwordencoder;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class BCryptCustomPasswordEncoder implements CustomPasswordEncoder {

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean noneMatchesPassword(String rawPassword, String encodedPassword) {
        return !matchesPassword(rawPassword, encodedPassword);
    }
}
