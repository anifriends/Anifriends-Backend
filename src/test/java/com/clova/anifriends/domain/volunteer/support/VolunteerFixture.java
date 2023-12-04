package com.clova.anifriends.domain.volunteer.support;

import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.util.ReflectionTestUtils;

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

    public static Volunteer volunteer(String imageUrl) {
        Volunteer volunteer = volunteer();
        if (imageUrl != null) {
            ReflectionTestUtils.setField(volunteer, "image",
                new VolunteerImage(volunteer, imageUrl));
        }
        return volunteer;
    }

    public static List<Volunteer> volunteers(int end) {
        MockPasswordEncoder passwordEncoder = new MockPasswordEncoder();
        return IntStream.range(0, end)
            .mapToObj(i -> {
                Volunteer volunteer = new Volunteer(
                    EMAIL, PASSWORD, BIRTH_DATE, PHONE_NUMBER, GENDER, NAME + i, passwordEncoder
                );
                ReflectionTestUtils.setField(volunteer, "image",
                    new VolunteerImage(volunteer, "imageUrl"));
                return volunteer;
            })
            .toList();
    }
}
