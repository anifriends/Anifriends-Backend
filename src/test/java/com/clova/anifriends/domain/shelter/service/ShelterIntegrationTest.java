package com.clova.anifriends.domain.shelter.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.dto.response.RegisterShelterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShelterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    ShelterService shelterService;

    @Autowired
    CustomPasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("updateShelterPassword 메서드 호출 시")
    class UpdateShelterPasswordTest {

        @Test
        @DisplayName("성공")
        void updateShelterPassword() {
            //given
            String email = "email@email.com";
            String oldRawPassword = "asdf123!";
            String shelterName = "shelterName";
            String address = "address";
            String addressDetail = "addressDetail";
            String phoneNumber = "010-1234-5678";
            String sparePhoneNumber = "010-1234-1234";
            boolean isOpenedAddress = true;

            RegisterShelterResponse registerShelterResponse = shelterService.registerShelter(email,
                oldRawPassword, shelterName, address,
                addressDetail, phoneNumber, sparePhoneNumber, isOpenedAddress);
            String newRawPassword = oldRawPassword + "a";

            //when
            shelterService.updatePassword(registerShelterResponse.shelterId(), oldRawPassword,
                newRawPassword);

            //then
            Shelter findShelter
                = entityManager.find(Shelter.class, registerShelterResponse.shelterId());
            boolean isPasswordUpdated
                = passwordEncoder.matchesPassword(newRawPassword, findShelter.getPassword());
            assertThat(isPasswordUpdated).isTrue();
        }
    }
}
