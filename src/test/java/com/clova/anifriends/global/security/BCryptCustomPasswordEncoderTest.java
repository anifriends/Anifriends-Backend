package com.clova.anifriends.global.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.global.security.passwordencoder.BCryptCustomPasswordEncoder;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BCryptCustomPasswordEncoderTest {

    CustomPasswordEncoder customPasswordEncoder
        = new BCryptCustomPasswordEncoder(new BCryptPasswordEncoder());

    @Nested
    @DisplayName("encode 메서드 호출 시")
    class EncodePasswordTest {

        @Test
        @DisplayName("성공: 암호화된 패스워드는 원시 패스워드와 같지 않음")
        void encodePassword() {
            //given
            String rawPassword = "asdf123!";

            //when
            String encodedPassword = customPasswordEncoder.encodePassword(rawPassword);

            //then
            assertThat(encodedPassword).isNotEqualTo(rawPassword);
        }

        @Test
        @DisplayName("성공: 암호화된 패스워드는 BCrypt 형식임")
        void encodedPasswordIsBCryptType() {
            //given
            String rawPassword = "asdf123!";
            String encodedPassword = customPasswordEncoder.encodePassword(rawPassword);
            Pattern bcryptPattern
                = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

            //when
            boolean isBCrypt = bcryptPattern.matcher(encodedPassword).matches();

            //then
            assertThat(isBCrypt).isTrue();
        }
    }

    @Nested
    @DisplayName("matches 메서드 호출 시")
    class MatchesPasswordTest {

        @Test
        @DisplayName("성공: 입력값으로 주어진 패스워드와 암호화된 패스워드와 같음")
        void matchesPasswordTrue() {
            //given
            String passwordForEncoded = "abcd123!";
            String encodedPassword = customPasswordEncoder.encodePassword(passwordForEncoded);
            String rawPassword = passwordForEncoded;

            //when
            boolean isPasswordEquals = customPasswordEncoder.matchesPassword(
                rawPassword,
                encodedPassword);

            //then
            assertThat(isPasswordEquals).isTrue();
        }

        @Test
        @DisplayName("성공: 입력값으로 주어진 패스워드와 암호화된 패스워드와 다름")
        void matchedPasswordFalse() {
            //given
            String passwordForEncoded = "abcd123!";
            String encodedPassword = customPasswordEncoder.encodePassword(passwordForEncoded);
            String rawPassword = passwordForEncoded + "a";

            //when
            boolean isPasswordEquals = customPasswordEncoder.matchesPassword(
                rawPassword,
                encodedPassword);

            //then
            assertThat(isPasswordEquals).isFalse();
        }
    }

    @Nested
    @DisplayName("noneMatchesPassword 메서드 호출 시")
    class noneMatchesPasswordTest {

        @Test
        @DisplayName("성공: 입력값으로 주어진 패스워드와 암호화된 패스워드와 다름")
        void noneMatchesPasswordTrue() {
            //given
            String passwordForEncoded = "abcd123!";
            String encodedPassword = customPasswordEncoder.encodePassword(passwordForEncoded);
            String rawPassword = passwordForEncoded + "a";

            //when
            boolean isPasswordEquals = customPasswordEncoder.noneMatchesPassword(
                rawPassword,
                encodedPassword);

            //then
            assertThat(isPasswordEquals).isTrue();
        }

        @Test
        @DisplayName("성공: 입력값으로 주어진 패스워드와 암호화된 패스워드와 같음")
        void noneMatchesPasswordFalse() {
            //given
            String passwordForEncoded = "abcd123!";
            String encodedPassword = customPasswordEncoder.encodePassword(passwordForEncoded);
            String rawPassword = passwordForEncoded;

            //when
            boolean isPasswordEquals = customPasswordEncoder.noneMatchesPassword(
                rawPassword,
                encodedPassword);

            //then
            assertThat(isPasswordEquals).isFalse();
        }
    }
}
