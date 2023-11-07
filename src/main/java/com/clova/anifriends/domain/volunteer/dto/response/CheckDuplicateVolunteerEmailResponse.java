package com.clova.anifriends.domain.volunteer.dto.response;

public record CheckDuplicateVolunteerEmailResponse(boolean isDuplicated) {

    public static CheckDuplicateVolunteerEmailResponse from(boolean isDuplicated) {
        return new CheckDuplicateVolunteerEmailResponse(isDuplicated);
    }
}
