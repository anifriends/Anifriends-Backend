package com.clova.anifriends.domain.shelter.dto.response;

import com.clova.anifriends.domain.shelter.Shelter;

public record FindShelterMyPageResponse(
    Long shelterId,
    String shelterName,
    String shelterEmail,
    String shelterAddress,
    String shelterAddressDetail,
    boolean shelterIsOpenedAddress,
    String shelterPhoneNumber,
    String shelterSparePhoneNumber,
    String shelterImageUrl
) {

    public static FindShelterMyPageResponse from(
        Shelter shelter
    ) {
        return new FindShelterMyPageResponse(
            shelter.getShelterId(),
            shelter.getName(),
            shelter.getEmail(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.isOpenedAddress(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getImage()
        );
    }
}
