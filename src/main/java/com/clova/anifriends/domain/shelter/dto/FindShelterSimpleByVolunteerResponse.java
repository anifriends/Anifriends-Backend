package com.clova.anifriends.domain.shelter.dto;

import com.clova.anifriends.domain.shelter.Shelter;

public record FindShelterSimpleByVolunteerResponse(
    String shelterName,
    String shelterEmail,
    String shelterAddress,
    String shelterImageUrl

) {

    public static FindShelterSimpleByVolunteerResponse from(Shelter shelter) {
        return new FindShelterSimpleByVolunteerResponse(
            shelter.getName(),
            shelter.getEmail(),
            shelter.getAddress(),
            shelter.getImageUrl()
        );
    }
}
