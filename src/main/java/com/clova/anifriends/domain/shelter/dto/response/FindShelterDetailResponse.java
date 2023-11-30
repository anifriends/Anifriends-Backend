package com.clova.anifriends.domain.shelter.dto.response;

import com.clova.anifriends.domain.shelter.Shelter;

public record FindShelterDetailResponse(
    Long shelterId,
    String shelterEmail,
    String shelterName,
    String shelterAddress,
    String shelterAddressDetail,
    String shelterPhoneNumber,
    String shelterSparePhoneNumber,
    String shelterImageUrl

) {

    public static FindShelterDetailResponse from(
        Shelter shelter
    ) {
        String addressDetail = "비공개";

        if (Boolean.TRUE.equals(shelter.isOpenedAddress())) {
            addressDetail = shelter.getAddressDetail();
        }

        return new FindShelterDetailResponse(
            shelter.getShelterId(),
            shelter.getEmail(),
            shelter.getName(),
            shelter.getAddress(),
            addressDetail,
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getImage()
        );
    }
}
