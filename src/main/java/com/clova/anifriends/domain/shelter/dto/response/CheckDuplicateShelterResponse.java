package com.clova.anifriends.domain.shelter.dto.response;

public record CheckDuplicateShelterResponse(
    boolean isDuplicated) {

    public static CheckDuplicateShelterResponse from(boolean isDuplicated) {
        return new CheckDuplicateShelterResponse(isDuplicated);
    }
}
