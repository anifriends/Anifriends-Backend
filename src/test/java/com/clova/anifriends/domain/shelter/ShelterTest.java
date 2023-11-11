package com.clova.anifriends.domain.shelter;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.MockImageRemover;
import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.common.ImageRemover;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShelterTest {

    CustomPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new MockPasswordEncoder();
    }

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
                isOpenedAddress, passwordEncoder);

            //then
            assertThat(shelter.getEmail()).isEqualTo(email);
            assertThat(passwordEncoder.matchesPassword(password, shelter.getPassword())).isTrue();
            assertThat(shelter.getAddress()).isEqualTo(address);
            assertThat(shelter.getAddressDetail()).isEqualTo(addressDetail);
            assertThat(shelter.getName()).isEqualTo(name);
            assertThat(shelter.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(shelter.getSparePhoneNumber()).isEqualTo(sparePhoneNumber);
            assertThat(shelter.isOpenedAddress()).isEqualTo(isOpenedAddress);
        }
    }

    @Nested
    @DisplayName("update 실행 시")
    class UpdateTest {

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 새로운 이미지 갱신")
        void updateWhenExistToNewImage() {
            // given
            ImageRemover imageRemover = new MockImageRemover();
            String originImageUrl = "originImageUrl";
            String newImageUrl = "newImageUrl";

            Shelter shelter = ShelterFixture.shelter(originImageUrl);

            // when
            shelter.updateShelter(
                shelter.getName(),
                newImageUrl,
                shelter.getAddress(),
                shelter.getAddressDetail(),
                shelter.getPhoneNumber(),
                shelter.getSparePhoneNumber(),
                shelter.isOpenedAddress(),
                imageRemover
            );

            // then
            assertThat(shelter.getImage()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 동일한 이미지")
        void updateWhenSame() {
            // given
            ImageRemover imageRemover = new MockImageRemover();
            String sameImageUrl = "originImageUrl";

            Shelter shelter = ShelterFixture.shelter(sameImageUrl);

            // when
            shelter.updateShelter(
                shelter.getName(),
                sameImageUrl,
                shelter.getAddress(),
                shelter.getAddressDetail(),
                shelter.getPhoneNumber(),
                shelter.getSparePhoneNumber(),
                shelter.isOpenedAddress(),
                imageRemover
            );

            // then
            assertThat(shelter.getImage()).isEqualTo(sameImageUrl);
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> null")
        void update() {
            // given
            ImageRemover imageRemover = new MockImageRemover();
            String originImageUrl = "originImageUrl";
            String nullNewImageUrl = null;

            Shelter shelter = ShelterFixture.shelter(originImageUrl);

            // when
            shelter.updateShelter(
                shelter.getName(),
                nullNewImageUrl,
                shelter.getAddress(),
                shelter.getAddressDetail(),
                shelter.getPhoneNumber(),
                shelter.getSparePhoneNumber(),
                shelter.isOpenedAddress(),
                imageRemover
            );

            // then
            assertThat(shelter.getImage()).isNull();
        }

        @Test
        @DisplayName("성공: null -> 새로운 이미지 갱신")
        void updateNoneToNewImage() {
            // given
            ImageRemover imageRemover = new MockImageRemover();
            String nullOriginImageUrl = null;
            String newImageUrl = "newImageUrl";

            Shelter shelter = ShelterFixture.shelter(nullOriginImageUrl);

            // when
            shelter.updateShelter(
                shelter.getName(),
                newImageUrl,
                shelter.getAddress(),
                shelter.getAddressDetail(),
                shelter.getPhoneNumber(),
                shelter.getSparePhoneNumber(),
                shelter.isOpenedAddress(),
                imageRemover
            );

            // then
            assertThat(shelter.getImage()).isEqualTo(newImageUrl);
        }

    }
}
