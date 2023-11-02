package com.clova.anifriends.domain.recruitment.dto.request;

import java.time.LocalDate;

public record FindRecruitmentByVolunteerRequest(
    String keyword,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isClosed,
    Boolean title,
    Boolean content,
    Boolean shelterName) {

}
