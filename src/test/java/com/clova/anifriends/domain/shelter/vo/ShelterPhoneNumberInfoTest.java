package com.clova.anifriends.domain.shelter.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ShelterPhoneNumberInfoTest {

    @Nested
    @DisplayName("ShelterPhoneNumber 생성 시")
    class NewShelterPhoneNumberTest {

        String phoneNumber = "010-1234-1234";
        String sparePhoneNumber = "010-1234-5678";

        @Test
        @DisplayName("성공")
        void newShelterPhoneNumber() {
            // given
            // when
            ShelterPhoneNumberInfo shelterPhoneNumberInfo = new ShelterPhoneNumberInfo(phoneNumber,
                sparePhoneNumber);

            // then
            assertThat(shelterPhoneNumberInfo.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(shelterPhoneNumberInfo.getSparePhoneNumber()).isEqualTo(sparePhoneNumber);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 전화번호가 null")
        void throwExceptionWhenPhoneNumberIsNull() {
            // given
            String nullPhoneNumber = null;

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(nullPhoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 임시 전화번호가 null")
        void throwExceptionWhenSparePhoneNumberIsNull() {
            // given
            String nullSparePhoneNumber = null;

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, nullSparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @ParameterizedTest
        @CsvSource({
            "01000-1234-5678", "010-123456-1234", "012-1234-123"
        })
        @DisplayName("예외(ShelterNotFoundException): 전화번호가 패턴에 맞지 않는 경우")
        void throwExceptionWhenPhoneNumberIsNotPatternNumber(String invalidPhoneNumber) {
            // given
            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(invalidPhoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @ParameterizedTest
        @CsvSource({
            "01000-1234-5678", "010-123456-1234", "012-1234-123"
        })
        @DisplayName("예외(ShelterNotFoundException): 임시 전화번호가 패턴에 맞지 않는 경우")
        void throwExceptionWhenSparePhoneNumberIsNotPatternNumber(String invalidSparePhoneNumber) {
            // given
            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, invalidSparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("update 실행 시")
    class Update {

        @Test
        @DisplayName("성공")
        void update() {
            // given
            String phoneNumberValue = "010-1234-1234";
            String sparePhoneNumberValue = "010-1234-1234";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumberValue, sparePhoneNumberValue));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 전화번호 형식이 아닌 경우")
        void exceptionWhenWrongPattern() {
            // given
            String phoneNumberValue = "01012341234";
            String sparePhoneNumberValue = "0101234-1234";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumberValue, sparePhoneNumberValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 전화번호가 null")
        void exceptionWhenPhoneNumberIsNull() {
            // given
            String nullPhoneNumberValue = null;
            String sparePhoneNumberValue = "010-1234-1234";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(nullPhoneNumberValue, sparePhoneNumberValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 임시 전화번호가 null")
        void exceptionWhenSparePhoneNumberIsNull() {
            // given
            String phoneNumberValue = "010-1234-1234";
            String nullSparePhoneNumberValue = null;

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumberValue, nullSparePhoneNumberValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 전화번호가 blank")
        void exceptionWhenPhoneNumberIsBlank() {
            // given
            String blankPhoneNumberValue = "";
            String sparePhoneNumberValue = "010-1234-1234";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(blankPhoneNumberValue, sparePhoneNumberValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 임시 전화번호가 blank")
        void exceptionWhenSparePhoneNumberIsBlank() {
            // given
            String phoneNumberValue = "010-1234-1234";
            String blankSparePhoneNumberValue = "";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumberValue, blankSparePhoneNumberValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }

}
