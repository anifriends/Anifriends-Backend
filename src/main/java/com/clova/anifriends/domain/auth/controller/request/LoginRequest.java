package com.clova.anifriends.domain.auth.controller.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank
    String email,
    @NotBlank
    String password) {

}
