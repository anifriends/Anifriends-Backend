package com.clova.anifriends.domain.shelter.dto;

import com.clova.anifriends.domain.shelter.Shelter;

public record FindShelterDetailResponse(
    Long shelterId,
    String email,
    String name,
    String address,
    String addressDetail,
    String phoneNumber,
    String sparePhoneNumber,
    String imageUrl

) {

    public static FindShelterDetailResponse from(
        Shelter shelter
    ) {
        return new FindShelterDetailResponse(
            shelter.getShelterId(),
            shelter.getEmail(),
            shelter.getName(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelter.getShelterImageUrl()
        );
    }
}
