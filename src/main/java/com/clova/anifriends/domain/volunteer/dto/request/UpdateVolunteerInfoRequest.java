package com.clova.anifriends.domain.volunteer.dto.request;

import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.time.LocalDate;

public record UpdateVolunteerInfoRequest(
    String name,
    VolunteerGender gender,
    LocalDate birthDate,
    String phoneNumber,
    String imageUrl) {

}
