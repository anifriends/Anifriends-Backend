package com.clova.anifriends.global.firebase.domain;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {

    private String token;
    private Notification notification;

    public static Message of(String targetToken, String title, String content,
        NotificationType type) {
        return new Message(targetToken, Notification.of(title, content, type));
    }
}
