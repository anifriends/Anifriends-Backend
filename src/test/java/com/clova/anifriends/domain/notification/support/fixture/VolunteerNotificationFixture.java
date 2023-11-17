package com.clova.anifriends.domain.notification.support.fixture;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.volunteer.Volunteer;

public class VolunteerNotificationFixture {

    public static final String title = "title";
    public static final String content = "content";
    public static final String notificationType = "VOLUNTEER_APPROVED";

    public static VolunteerNotification volunteerNotification(Volunteer volunteer) {
        return new VolunteerNotification(
            volunteer,
            title,
            content,
            notificationType
        );
    }
}
