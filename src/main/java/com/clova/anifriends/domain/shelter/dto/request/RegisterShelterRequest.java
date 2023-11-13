package com.clova.anifriends.domain.shelter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterShelterRequest(
    @NotBlank(message = "이메일은 필수값입니다.")
    String email,
    @NotBlank(message = "패스워드는 필수값입니다.")
    String password,
    @NotBlank(message = "보호소 이름은 필수값입니다.")
    String name,
    @NotBlank(message = "보호소 주소는 필수값입니다.")
    String address,
    @NotBlank(message = "보호소 상세 주소는 필수값입니다.")
    String addressDetail,
    @NotBlank(message = "보호소 전화번호는 필수값입니다.")
    String phoneNumber,
    @NotBlank(message = "보호소 임시 전화번호는 필수값입니다.")
    String sparePhoneNumber,
    @NotNull(message = "보호소 주소 공개 여부는 필수값입니다.")
    Boolean isOpenedAddress
) {

}
