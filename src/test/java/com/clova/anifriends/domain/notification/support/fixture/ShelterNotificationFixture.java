package com.clova.anifriends.domain.notification.support.fixture;

import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.shelter.Shelter;

public class ShelterNotificationFixture {

    public static final String title = "title";
    public static final String content = "content";
    public static final String notificationType = "NEW_APPLICANT";

    public static ShelterNotification shelterNotification(Shelter shelter) {
        return new ShelterNotification(
            shelter,
            title,
            content,
            notificationType
        );
    }
}
