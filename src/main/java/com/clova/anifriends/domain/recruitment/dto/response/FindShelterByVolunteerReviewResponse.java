package com.clova.anifriends.domain.recruitment.dto.response;

import com.clova.anifriends.domain.recruitment.Recruitment;

public record FindShelterByVolunteerReviewResponse(
    String name,
    String email,
    String address,
    String imageUrl

) {

    public static FindShelterByVolunteerReviewResponse from(Recruitment recruitment) {
        return new FindShelterByVolunteerReviewResponse(
            recruitment.getShelter().getName(),
            recruitment.getShelter().getEmail(),
            recruitment.getShelter().getAddress(),
            recruitment.getShelter().getImageUrl()
        );
    }
}
