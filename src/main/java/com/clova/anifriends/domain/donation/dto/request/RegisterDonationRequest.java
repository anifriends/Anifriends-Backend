package com.clova.anifriends.domain.donation.dto.request;

public record RegisterDonationRequest(
    Long shelterId,
    Integer amount
) {

}
