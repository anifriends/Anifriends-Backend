package com.clova.anifriends.domain.notification;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.notification.vo.NotificationContent;
import com.clova.anifriends.domain.notification.vo.NotificationRead;
import com.clova.anifriends.domain.notification.vo.NotificationTitle;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.shelter.Shelter;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shelter_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShelterNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_notification_id")
    private Long shelterNotificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Embedded
    private NotificationTitle title;

    @Embedded
    private NotificationContent content;

    @Embedded
    private NotificationRead isRead = new NotificationRead(false);

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;

    public ShelterNotification(
        Shelter shelter,
        String title,
        String content,
        String type
    ) {
        this.shelter = shelter;
        this.title = new NotificationTitle(title);
        this.content = new NotificationContent(content);
        this.type = NotificationType.valueOf(type);
    }

    public Long getShelterNotificationId() {
        return shelterNotificationId;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public String getTitle() {
        return title.getTitle();
    }

    public String getContent() {
        return content.getContent();
    }

    public Boolean getIsRead() {
        return isRead.getIsRead();
    }

    public NotificationType getType() {
        return type;
    }
}
