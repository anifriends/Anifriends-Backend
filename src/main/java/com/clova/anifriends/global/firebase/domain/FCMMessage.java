package com.clova.anifriends.global.firebase.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMMessage {

    private boolean validateOnly;
    private Message message;

    public static FCMMessage makeMessage(String targetToken, String title, String content,
        NotificationType type) {
        return new FCMMessage(false, Message.of(targetToken, title, content, type));
    }
}
