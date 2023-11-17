package com.clova.anifriends.domain.shelter.dto.response;

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
