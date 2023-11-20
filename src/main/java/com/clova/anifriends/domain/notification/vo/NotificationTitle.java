package com.clova.anifriends.domain.notification.vo;

import com.clova.anifriends.domain.notification.exception.NotificationBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTitle {

    @Column(name = "title")
    private String title;

    public NotificationTitle(String value) {
        validateNotNull(value);
        this.title = value;
    }

    private void validateNotNull(String value) {
        if (Objects.isNull(value)) {
            throw new NotificationBadRequestException("알림 제목은 필수값입니다.");
        }
    }
}
