package com.clova.anifriends.global.firebase.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Notification {

    private String title;
    private String content;
    private NotificationType type;

    public static Notification of(String title, String content, NotificationType type) {
        return new Notification(title, content, type);
    }
}
