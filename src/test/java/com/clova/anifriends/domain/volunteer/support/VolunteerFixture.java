package com.clova.anifriends.domain.volunteer.support;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerGender;

public class VolunteerFixture {

    private static final String EMAIL = "asdf@gmail.com";
    public static final String PASSWORD = "asdf1234";
    private static final String BIRTH_DATE = "1999-03-23";
    private static final String PHONE_NUMBER = "010-1234-5678";
    private static final String GENDER = VolunteerGender.MALE.getName();
    private static final String NAME = "김봉사";
    private static final CustomPasswordEncoder PASSWORD_ENCODER = new MockPasswordEncoder();

    public static Volunteer volunteer() {
        return new Volunteer(EMAIL, PASSWORD, BIRTH_DATE, PHONE_NUMBER, GENDER, NAME,
            PASSWORD_ENCODER);
    }
}
