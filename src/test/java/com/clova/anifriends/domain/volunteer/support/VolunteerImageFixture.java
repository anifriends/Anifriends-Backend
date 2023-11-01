package com.clova.anifriends.domain.volunteer.support;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;

public class VolunteerImageFixture {

    private static final String IMAGE_URL = "image/url";

    public static VolunteerImage volunteerImage(Volunteer volunteer) {
        return new VolunteerImage(volunteer, IMAGE_URL);
    }
}
