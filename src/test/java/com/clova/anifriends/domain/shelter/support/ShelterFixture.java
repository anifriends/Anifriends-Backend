package com.clova.anifriends.domain.shelter.support;

import static java.util.Objects.nonNull;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.util.ReflectionTestUtils;

public class ShelterFixture {

    private static final String SHELTER_EMAIL = "shelterEmail@email.com";
    public static final String RAW_SHELTER_PASSWORD = "shelterPassword";
    private static final String SHELTER_ADDRESS = "shelterAddress";
    private static final String SHELTER_ADDRESS_DETAIL = "shelterAddressDetail";
    private static final String SHELTER_NAME = "shelterName";
    private static final String PHONE_NUMBER = "010-1234-5678";
    private static final String SPARE_PHONE_NUMBER = "010-8765-4321";
    private static final boolean IS_OPENED_ADDRESS = true;
    private static final String SHELTER_IMAGE_URL = "www.aws.s3.com/2";
    private static final CustomPasswordEncoder PASSWORD_ENCODER = new MockPasswordEncoder();

    public static Shelter shelter() {
        return new Shelter(
            SHELTER_EMAIL,
            RAW_SHELTER_PASSWORD,
            SHELTER_ADDRESS,
            SHELTER_ADDRESS_DETAIL,
            SHELTER_NAME,
            PHONE_NUMBER,
            SPARE_PHONE_NUMBER,
            IS_OPENED_ADDRESS,
            PASSWORD_ENCODER
        );
    }

    public static Shelter shelter(String imageUrl) {
        Shelter shelter = new Shelter(
            SHELTER_EMAIL,
            RAW_SHELTER_PASSWORD,
            SHELTER_ADDRESS,
            SHELTER_ADDRESS_DETAIL,
            SHELTER_NAME,
            PHONE_NUMBER,
            SPARE_PHONE_NUMBER,
            IS_OPENED_ADDRESS,
            PASSWORD_ENCODER
        );
        if (nonNull(imageUrl)) {
            ReflectionTestUtils.setField(shelter, "image", new ShelterImage(shelter, imageUrl));
        }
        
        return shelter;
    }

    public static List<Shelter> shelters(int end) {
        List<Shelter> shelters = IntStream.range(0, end)
            .mapToObj(i -> new Shelter(
                SHELTER_EMAIL,
                RAW_SHELTER_PASSWORD,
                SHELTER_ADDRESS,
                SHELTER_ADDRESS_DETAIL,
                SHELTER_NAME + i,
                PHONE_NUMBER, SPARE_PHONE_NUMBER,
                IS_OPENED_ADDRESS,
                PASSWORD_ENCODER))
            .toList();
        shelters.forEach(shelter -> {
            ReflectionTestUtils.setField(shelter, "image",
                new ShelterImage(shelter, SHELTER_IMAGE_URL));
        });
        return shelters;
    }

    public static List<Shelter> createShelters(int end) {
        return IntStream.range(0, end)
            .mapToObj(i -> new Shelter(
                SHELTER_EMAIL,
                RAW_SHELTER_PASSWORD,
                SHELTER_ADDRESS,
                SHELTER_ADDRESS_DETAIL,
                SHELTER_NAME + i,
                PHONE_NUMBER, SPARE_PHONE_NUMBER,
                IS_OPENED_ADDRESS,
                PASSWORD_ENCODER))
            .toList();
    }
}
