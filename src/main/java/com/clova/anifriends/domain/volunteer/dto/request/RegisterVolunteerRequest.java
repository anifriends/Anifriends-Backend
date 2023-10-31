package com.clova.anifriends.domain.volunteer.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterVolunteerRequest(
    @NotBlank(message = "이메일은 필수 항목입니다.") String email,
    @NotBlank(message = "비밀번호는 필수 항목입니다.") String password,
    @NotBlank(message = "이름은 필수 항목입니다.") String name,
    @NotBlank(message = "생년월일은 필수 항목입니다.") String birthDate,
    @NotBlank(message = "전화번호는 필수 항목입니다.") String phoneNumber,
    @NotBlank(message = "성별은 필수 항목입니다.") String gender
) {

}
