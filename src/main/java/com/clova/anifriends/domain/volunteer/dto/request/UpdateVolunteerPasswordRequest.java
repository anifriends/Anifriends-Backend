package com.clova.anifriends.domain.volunteer.dto.request;

public record UpdateVolunteerPasswordRequest(
    String oldPassword,
    String newPassword
) {

}
