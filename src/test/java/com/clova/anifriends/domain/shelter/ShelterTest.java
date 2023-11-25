package com.clova.anifriends.domain.shelter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.exception.ShelterBadRequestException;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.Optional;
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
        @DisplayName("성공")
        void updateWhen() {
            // given
            String originName = "originName";
            String originAddress = "originAddress";
            String originAddressDetail = "originAddressDetail";
            String originPhoneNumber = "010-1111-1111";
            String originSparePhoneNumber = "010-2222-2222";
            boolean originIsOpenedAddress = true;

            String newName = "newName";
            String newImageUrl = "newImageUrl";
            String newAddress = "newAddress";
            String newAddressDetail = "newAddressDetail";
            String newPhoneNumber = "010-3333-3333";
            String newSparePhoneNumber = "010-4444-4444";
            boolean newIsOpenedAddress = false;

            Shelter shelter = new Shelter(
                "shelterEmail@email.com",
                "shelterPassword",
                originAddress,
                originAddressDetail,
                originName,
                originPhoneNumber,
                originSparePhoneNumber,
                originIsOpenedAddress,
                passwordEncoder
            );

            // when
            shelter.updateShelter(
                newName,
                newImageUrl,
                newAddress,
                newAddressDetail,
                newPhoneNumber,
                newSparePhoneNumber,
                newIsOpenedAddress
            );

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(shelter.getName()).isEqualTo(newName);
                softAssertions.assertThat(shelter.getImage()).isEqualTo(newImageUrl);
                softAssertions.assertThat(shelter.getAddress()).isEqualTo(newAddress);
                softAssertions.assertThat(shelter.getAddressDetail()).isEqualTo(newAddressDetail);
                softAssertions.assertThat(shelter.getPhoneNumber()).isEqualTo(newPhoneNumber);
                softAssertions.assertThat(shelter.isOpenedAddress()).isEqualTo(newIsOpenedAddress);
                softAssertions.assertThat(shelter.getSparePhoneNumber())
                    .isEqualTo(newSparePhoneNumber);
            });
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 새로운 이미지 갱신")
        void updateWhenExistToNewImage() {
            // given
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
                shelter.isOpenedAddress()
            );

            // then
            assertThat(shelter.getImage()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 동일한 이미지")
        void updateWhenSame() {
            // given
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
                shelter.isOpenedAddress()
            );

            // then
            assertThat(shelter.getImage()).isEqualTo(sameImageUrl);
        }

        @Test
        @DisplayName("성공: 기존 이미지 존재 -> 이미지 none")
        void updateExistToNone() {
            // given
            String originImageUrl = "originImageUrl";
            String blankImageUrl = "";

            Shelter shelter = ShelterFixture.shelter(originImageUrl);

            // when
            shelter.updateShelter(
                shelter.getName(),
                blankImageUrl,
                shelter.getAddress(),
                shelter.getAddressDetail(),
                shelter.getPhoneNumber(),
                shelter.getSparePhoneNumber(),
                shelter.isOpenedAddress()
            );

            // then
            assertThat(shelter.getImage()).isBlank();
        }

        @Test
        @DisplayName("성공: 이미지 none -> 새로운 이미지 갱신")
        void updateNoneToNewImage() {
            // given
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
                shelter.isOpenedAddress()
            );

            // then
            assertThat(shelter.getImage()).isEqualTo(newImageUrl);
        }

        @Test
        @DisplayName("성공: 이미지 none -> 이미지 none")
        void updateNoneToNone() {
            // given
            String nullImageUrl = null;

            Shelter shelter = ShelterFixture.shelter(nullImageUrl);

            // when
            shelter.updateShelter(
                shelter.getName(),
                nullImageUrl,
                shelter.getAddress(),
                shelter.getAddressDetail(),
                shelter.getPhoneNumber(),
                shelter.getSparePhoneNumber(),
                shelter.isOpenedAddress()
            );

            // then
            assertThat(shelter.getImage()).isBlank();
        }

    }

    @Nested
    @DisplayName("findImageToDelete 실행 시")
    class findImageToDelete {

        @Test
        @DisplayName("성공: 기존의 이미지가 존재하고 새로운 이미지와 다를 경우 기존의 이미지를 반환")
        void findImageToDeleteWhenDifferentFromNow() {
            // given
            String originImageUrl = "originImageUrl";
            String newImageUrl = "newImageUrl";

            Shelter shelter = ShelterFixture.shelter(originImageUrl);

            // when
            Optional<String> result = shelter.findImageToDelete(newImageUrl);

            // then
            assertThat(result).isEqualTo(Optional.of(originImageUrl));
        }

        @Test
        @DisplayName("성공: 기존의 이미지가 존재하고 새로운 이미지와 같을 경우 null반환")
        void findImageToDeleteWhenSameWithNow() {
            // given
            String sameImageUrl = "sameImageUrl";

            Shelter shelter = ShelterFixture.shelter(sameImageUrl);

            // when
            Optional<String> result = shelter.findImageToDelete(sameImageUrl);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("성공: 기존의 이미지가 존재하지 않으면 null반환")
        void findImageToDeleteWhenNowIsNull() {
            // given
            String newImageUrl = "newImageUrl";
            Shelter shelter = ShelterFixture.shelter();

            // when
            Optional<String> result = shelter.findImageToDelete(newImageUrl);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateDeviceToken 실행 시")
    class UpdateDeviceTokenTest {

        @Test
        @DisplayName("성공")
        void updateDeviceToken() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            String updateToken = "update";

            // when
            shelter.updateDeviceToken(updateToken);

            // then
            assertThat(shelter.getDeviceToken()).isEqualTo(updateToken);
        }

        @Test
        @DisplayName("예외(ShelterBadRequestException): 토큰이 null인 경우")
        void throwExceptionWhenTokenIsNull() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            String updateToken = null;

            // when
            Exception exception = catchException(() -> shelter.updateDeviceToken(updateToken));

            // then
            assertThat(exception).isInstanceOf(ShelterBadRequestException.class);
        }
    }
}
