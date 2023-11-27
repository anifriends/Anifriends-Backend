package com.clova.anifriends.domain.shelter.dto.response;

import com.clova.anifriends.domain.shelter.Shelter;

public record RegisterShelterResponse(Long shelterId) {

    public static RegisterShelterResponse from(Shelter shelter) {
        return new RegisterShelterResponse(shelter.getShelterId());
    }
}
