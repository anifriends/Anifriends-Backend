package com.clova.anifriends.domain.shelter.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterAddressInfoTest {

    @Nested
    @DisplayName("ShelterAddressInfo 생성 시")
    class NewShelterAddressInfoTest {

        String address = "address";
        String addressDetail = "addressDetail";
        boolean isOpenedAddress = false;

        @Test
        @DisplayName("성공")
        void newShelterAddressInfo() {
            //given
            //when
            ShelterAddressInfo shelterAddressInfo = new ShelterAddressInfo(address, addressDetail,
                isOpenedAddress);

            //then
            assertThat(shelterAddressInfo.getAddress()).isEqualTo(address);
            assertThat(shelterAddressInfo.getAddressDetail()).isEqualTo(addressDetail);
            assertThat(shelterAddressInfo.isOpenedAddress()).isEqualTo(isOpenedAddress);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 보호소 주소가 null")
        void exceptionWhenShelterAddressIsNull() {
            //given
            String nullAddress = null;

            //when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(nullAddress, addressDetail, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 보호소 상세 주소가 null")
        void exceptionWhenShelterAddressDetailIsNull() {
            //given
            String nullAddressDetail = null;

            //when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(address, nullAddressDetail, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 보호소 주소가 공백")
        void exceptionWhenShelterAddressIsBlank() {
            //given
            String blankAddress = "";

            //when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(blankAddress, addressDetail, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 보호소 상세 주소가 공백")
        void exceptionWhenShelterAddressDetailIsBlank() {
            //given
            String blankAddressDetail = "";

            //when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(address, blankAddressDetail, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 보호소 주소가 100자 초과")
        void exceptionWhenShelterAddressLengthOver100() {
            //given
            String addressLengthOver100 = "a".repeat(101);

            //then
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressLengthOver100, addressDetail, isOpenedAddress));

            //then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 보호소 상세 주소가 100자 초과")
        void exceptionWhenShelterAddressDetailLengthOver100() {
            //given
            String addressDetailLengthOver100 = "a".repeat(101);

            //then
            Exception exception = catchException(
                () -> new ShelterAddressInfo(address, addressDetailLengthOver100, isOpenedAddress));

            //then
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
            String addressValue = "address";
            String addressDetail = "addressDetail";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 주소 길이가 1")
        void updateWhenAddressLengthIs1() {
            // given
            String addressValue = "a";
            String addressDetail = "addressDetail";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 상세주소 길이가 1")
        void updateWhenDetailAddressLengthIs1() {
            // given
            String addressValue = "address";
            String addressDetail = "a";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 주소 길이가 100")
        void updateWhenAddressLengthIs100() {
            // given
            String addressValue = "a".repeat(100);
            String addressDetail = "addressDetail";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공: 상세주소 길이가 100")
        void updateWhenDetailAddressLengthIs100() {
            // given
            String addressValue = "address";
            String addressDetail = "a".repeat(100);
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 주소가 null")
        void exceptionWhenAddressIsNull() {
            // given
            String nullAddressValue = null;
            String addressDetail = "addressDetail";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(nullAddressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 상세 주소가 null")
        void exceptionWhenAddressDetailIsNull() {
            // given
            String addressValue = "address";
            String nullAddressDetail = null;
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, nullAddressDetail, isOpenedValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 주소가 빈 문자열")
        void exceptionWhenAddressIsBlank() {
            // given
            String blankAddressValue = "";
            String addressDetail = "addressDetail";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(blankAddressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 상세 주소가 빈 문자열")
        void exceptionWhenAddressDetailIsBlank() {
            // given
            String addressValue = "address";
            String blankAddressDetail = "";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, blankAddressDetail, isOpenedValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 주소 길이가 101")
        void exceptionWhenAddressLengthOver100() {
            // given
            String addressValue = "a".repeat(101);
            String addressDetail = "addressDetail";
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 상세 주소 길이가 101")
        void exceptionWhen() {
            // given
            String addressValue = "address";
            String addressDetail = "a".repeat(101);
            boolean isOpenedValue = true;

            // when
            Exception exception = catchException(
                () -> new ShelterAddressInfo(addressValue, addressDetail, isOpenedValue));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }

    }
}
