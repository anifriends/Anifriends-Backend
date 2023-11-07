package com.clova.anifriends.domain.shelter.dto;

public record CheckDuplicateEmailResponse(
    boolean isDuplicated) {

    public static CheckDuplicateEmailResponse from(boolean isDuplicated) {
        return new CheckDuplicateEmailResponse(isDuplicated);
    }
}
