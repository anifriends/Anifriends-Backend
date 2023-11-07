package com.clova.anifriends.domain.shelter.wrapper;

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
}