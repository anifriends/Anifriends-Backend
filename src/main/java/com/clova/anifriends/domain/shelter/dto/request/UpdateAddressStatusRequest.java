package com.clova.anifriends.domain.shelter.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateAddressStatusRequest(
    @NotNull(message = "보호소 주소 공개 여부는 필수값입니다.") Boolean isOpenedAddress
) {

}
