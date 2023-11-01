package com.clova.anifriends.domain.shelter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterTest {

    @Nested
    @DisplayName("Shelter 생성 시")
    class NewShelterTest {

        @Test
        @DisplayName("성공")
        void newShelter() {
            //given
            String email = "eamil@email.com";
            String password = "password123!";
            String address = "address";
            String addressDetail = "addressDetail";
            String name = "name";
            String phoneNumber = "010-1234-5678";
            String sparePhoneNumber = "010-1234-5678";
            boolean isOpenedAddress = false;

            //when
            Shelter shelter = new Shelter(
                email, password, address, addressDetail, name, phoneNumber, sparePhoneNumber,
                isOpenedAddress);

            //then
            assertThat(shelter.getEmail()).isEqualTo(email);
            assertThat(shelter.getPassword()).isEqualTo(password);
            assertThat(shelter.getAddress()).isEqualTo(address);
            assertThat(shelter.getAddressDetail()).isEqualTo(addressDetail);
            assertThat(shelter.getName()).isEqualTo(name);
            assertThat(shelter.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(shelter.getSparePhoneNumber()).isEqualTo(sparePhoneNumber);
            assertThat(shelter.isOpenedAddress()).isEqualTo(isOpenedAddress);
        }
    }
}
