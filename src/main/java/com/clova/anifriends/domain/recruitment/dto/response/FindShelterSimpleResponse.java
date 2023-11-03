package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;

public record FindShelterSimpleResponse(
    String name,
    String email,
    String address,
    String imageUrl

) {

    public static FindShelterSimpleResponse from(Recruitment recruitment) {
        return new FindShelterSimpleResponse(
            recruitment.getShelter().getName(),
            recruitment.getShelter().getEmail(),
            recruitment.getShelter().getAddress(),
            recruitment.getShelter().getImageUrl()
        );
    }
}
