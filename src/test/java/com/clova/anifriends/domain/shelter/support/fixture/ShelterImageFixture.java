package com.clova.anifriends.domain.shelter.support.fixture;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;

public class ShelterImageFixture {

    public static final String IMAGE_URL = "www.aws.s3.com/2";

    public static ShelterImage shelterImage(Shelter shelter) {
        return new ShelterImage(
            shelter,
            IMAGE_URL
        );
    }
}
