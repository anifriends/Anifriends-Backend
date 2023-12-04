package com.clova.anifriends.domain.notification;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.notification.vo.NotificationContent;
import com.clova.anifriends.domain.notification.vo.NotificationRead;
import com.clova.anifriends.domain.notification.vo.NotificationTitle;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.volunteer.Volunteer;
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
@Table(name = "volunteer_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VolunteerNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_notification_id")
    private Long volunteerNotificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Embedded
    private NotificationTitle title;

    @Embedded
    private NotificationContent content;

    @Embedded
    private NotificationRead isRead = new NotificationRead(false);

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;

    public VolunteerNotification(
        Volunteer volunteer,
        String title,
        String content,
        String type
    ) {
        this.volunteer = volunteer;
        this.title = new NotificationTitle(title);
        this.content = new NotificationContent(content);
        this.type = NotificationType.valueOf(type);
    }

    public Long getVolunteerNotificationId() {
        return volunteerNotificationId;
    }

    public Volunteer getVolunteer() {
        return volunteer;
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
