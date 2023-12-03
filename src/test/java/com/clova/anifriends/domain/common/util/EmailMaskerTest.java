package com.clova.anifriends.domain.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmailMaskerTest {

    @Nested
    @DisplayName("masking 메서드 호출 시")
    class MaskingTest {

        @Test
        @DisplayName("성공")
        void masking() {
            //given
            String email = "email@email.com";

            //when
            String masking = EmailMasker.masking(email);

            //then
            assertThat(masking).isEqualTo("emai****");
        }

        @ParameterizedTest
        @CsvSource({"asdf", "@.com"})
        @DisplayName("예외(CommonBadRequestException): 입력값이 이메일 형식이 아님")
        void exceptionWHenInputIsNotEmailPattern(String email) {
            //given
            //when
            Exception exception = catchException(() -> EmailMasker.masking(email));

            //then
            assertThat(exception).isInstanceOf(CommonBadRequestException.class);
        }
    }
}