package com.clova.anifriends.domain.shelter.dto;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;

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

    public static FindShelterDetailResponse of(
        Shelter shelter, ShelterImage shelterImage
    ) {
        return new FindShelterDetailResponse(
            shelter.getShelterId(),
            shelter.getEmail(),
            shelter.getName(),
            shelter.getAddress(),
            shelter.getAddressDetail(),
            shelter.getPhoneNumber(),
            shelter.getSparePhoneNumber(),
            shelterImage.getImageUrl()
        );
    }
}
