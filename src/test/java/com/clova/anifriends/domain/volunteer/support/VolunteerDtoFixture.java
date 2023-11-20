package com.clova.anifriends.domain.volunteer.support;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;
import com.clova.anifriends.domain.volunteer.dto.request.CheckDuplicateVolunteerEmailRequest;
import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.dto.response.FindVolunteerMyPageResponse;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;

public class VolunteerDtoFixture {

    private static final Long VOLUNTEER_ID = 1L;
    private static final String EMAIL = "asdf@gmail.com";
    private static final String PASSWORD = "asdf1234";
    private static final String BIRTH_DATE = "1999-03-23";
    private static final String PHONE_NUMBER = "010-1234-5678";
    private static final String GENDER = VolunteerGender.MALE.getName();
    private static final Integer TEMPERATURE = 36;
    private static final String NAME = "김봉사";
    private static final String IMAGE_URL = "image/url";
    private static final Long VOLUNTEER_COUNT = 2L;
    private static final Volunteer volunteer = VolunteerFixture.volunteer();
    private static final VolunteerImage volunteerImage = VolunteerImageFixture.volunteerImage(
        volunteer);

    public static RegisterVolunteerRequest registerVolunteerRequest() {
        return new RegisterVolunteerRequest(EMAIL, PASSWORD, NAME, BIRTH_DATE, PHONE_NUMBER,
            GENDER);
    }

    public static CheckDuplicateVolunteerEmailRequest checkDuplicateVolunteerEmailRequest() {
        return new CheckDuplicateVolunteerEmailRequest(EMAIL);
    }

    public static FindVolunteerMyPageResponse findVolunteerMyPageResponse(Volunteer volunteer) {
        return FindVolunteerMyPageResponse.from(volunteer);
    }
}

