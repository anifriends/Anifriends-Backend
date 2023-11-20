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
public class NotificationRead {

    @Column(name = "is_read")
    private Boolean isRead;

    public NotificationRead(Boolean value) {
        validateIsRead(value);
        this.isRead = value;
    }

    public void validateIsRead(Boolean isRead) {
        if (Objects.isNull(isRead)) {
            throw new NotificationBadRequestException("알림 읽음 여부는 필수입니다.");
        }
    }
}
