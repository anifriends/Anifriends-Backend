package com.clova.anifriends.domain.shelter.dto.request;

public record UpdateShelterRequest(
    String name,
    String imageUrl,
    String address,
    String addressDetail,
    String phoneNumber,
    String sparePhoneNumber,
    Boolean isOpenedAddress
) {

}
