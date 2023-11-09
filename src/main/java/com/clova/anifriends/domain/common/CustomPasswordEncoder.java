package com.clova.anifriends.domain.common;

public interface CustomPasswordEncoder {

    String encodePassword(String rawPassword);

    boolean matchesPassword(String rawPassword, String encodedPassword);

    boolean noneMatchesPassword(String rawPassword, String encodedPassword);
}
