package com.clova.anifriends.docs.format;

import jakarta.validation.constraints.NotNull;

public record TestRequest(
    @NotNull
    String required,
    String optional) {

}
