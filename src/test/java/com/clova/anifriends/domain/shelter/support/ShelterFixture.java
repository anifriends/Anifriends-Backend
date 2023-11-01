package com.clova.anifriends.domain.shelter.support;

import com.clova.anifriends.domain.shelter.Shelter;

public class ShelterFixture {

    private static final String SHELTER_EMAIL = "shelterEmail@email.com";
    private static final String SHELTER_PASSWORD = "shelterPassword";
    private static final String SHELTER_ADDRESS = "shelterAddress";
    private static final String SHELTER_ADDRESS_DETAIL = "shelterAddressDetail";
    private static final String SHELTER_NAME = "보호소이름";
    private static final String PHONE_NUMBER = "010-1234-5678";
    private static final String SPARE_PHONE_NUMBER = "010-8765-4321";
    private static final boolean IS_OPENED_ADDRESS = true;

    public static Shelter shelter() {
        return new Shelter(
            SHELTER_EMAIL,
            SHELTER_PASSWORD,
            SHELTER_ADDRESS,
            SHELTER_ADDRESS_DETAIL,
            SHELTER_NAME,
            PHONE_NUMBER,
            SPARE_PHONE_NUMBER,
            IS_OPENED_ADDRESS
        );
    }
}
