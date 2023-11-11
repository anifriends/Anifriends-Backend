package com.clova.anifriends.domain.volunteer.dto.request;

public record UpdateVolunteerPassword(
    String oldPassword,
    String newPassword
) {

}
