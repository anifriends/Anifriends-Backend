package com.clova.anifriends.domain.shelter.dto;

import com.clova.anifriends.domain.shelter.Shelter;

public record FindShelterMyPageResponse(
    Long shelterId,
    String name,
    String address,
    String addressDetail,
    boolean isOpenedAddress,
    String phoneNumber,
    String sparePhoneNumber,
    String imageUrl
) {

    public static FindShelterMyPageResponse from(
        Shelter shelter
    ) {
        return new FindShelterMyPageResponse(
            shelter.getShelterId(),
            shelter.getName(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.isOpenedAddress(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getShelterImageUrl()
        );
    }
}
