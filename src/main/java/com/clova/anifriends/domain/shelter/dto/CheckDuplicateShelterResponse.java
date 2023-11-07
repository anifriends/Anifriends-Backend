package com.clova.anifriends.domain.shelter.dto;

public record CheckDuplicateShelterResponse(
    boolean isDuplicated) {

    public static CheckDuplicateShelterResponse from(boolean isDuplicated) {
        return new CheckDuplicateShelterResponse(isDuplicated);
    }
}
