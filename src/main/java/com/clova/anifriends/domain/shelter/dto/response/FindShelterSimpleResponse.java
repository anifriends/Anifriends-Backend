package com.clova.anifriends.domain.shelter.dto.response;

import com.clova.anifriends.domain.shelter.Shelter;

public record FindShelterSimpleResponse(
    String shelterName,
    String shelterEmail,
    String shelterAddress,
    String shelterImageUrl

) {

    public static FindShelterSimpleResponse from(Shelter shelter) {
        return new FindShelterSimpleResponse(
            shelter.getName(),
            shelter.getEmail(),
            shelter.getAddress(),
            shelter.getImage()
        );
    }
}
