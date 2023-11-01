package com.clova.anifriends.domain.shelter.wrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterPhoneNumberInfoTest {

    @Nested
    @DisplayName("ShelterPhoneNumber 생성 시")
    class newShelterPhoneNumberTest {

        String phoneNumber;
        String sparePhoneNumber;

        @Test
        @DisplayName("성공")
        void newShelterPhoneNumber() {
            // given
            phoneNumber = "010-1234-1234";
            sparePhoneNumber = "010-1234-1253";

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
            phoneNumber = null;
            sparePhoneNumber = "010-1234-1253";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 임시 전화번호가 null")
        void throwExceptionWhenSparePhoneNumberIsNull() {
            // given
            phoneNumber = "010-1234-1253";
            sparePhoneNumber = null;

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 전화번호가 빈 칸")
        void throwExceptionWhenPhoneNumberIsBlank() {
            // given
            phoneNumber = "";
            sparePhoneNumber = "010-1243-1234";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 임시 전화번호가 빈 칸")
        void throwExceptionWhenSparePhoneNumberIsBlank() {
            // given
            phoneNumber = "010-1243-1234";
            sparePhoneNumber = "";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 전화번호가 패턴에 맞지 않는 경우")
        void throwExceptionWhenPhoneNumberIsNotPatternNumber() {
            // given
            phoneNumber = "010-1243-12345";
            sparePhoneNumber = "010-2312-1232";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 임시 전화번호가 패턴에 맞지 않는 경우")
        void throwExceptionWhenSparePhoneNumberIsNotPatternNumber() {
            // given
            phoneNumber = "010-1243-1235";
            sparePhoneNumber = "010-2312-12312";

            // when
            Exception exception = catchException(
                () -> new ShelterPhoneNumberInfo(phoneNumber, sparePhoneNumber));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }
}