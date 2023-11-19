package com.clova.anifriends.domain.notification.service;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.VolunteerNotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VolunteerNotificationService {

    private final VolunteerNotificationRepository volunteerNotificationRepository;

    @Transactional(readOnly = true)
    public FindVolunteerNotificationsResponse findVolunteerNotifications(Long volunteerId) {
        List<VolunteerNotification> volunteerNotifications = volunteerNotificationRepository.findByVolunteer_VolunteerIdOrderByCreatedAtDesc(
            volunteerId);
        return FindVolunteerNotificationsResponse.from(volunteerNotifications);
    }

    @Transactional(readOnly = true)
    public FindVolunteerHasNewNotificationResponse findVolunteerHasNewNotification(Long volunteerId) {
        return FindVolunteerHasNewNotificationResponse.from(
            volunteerNotificationRepository.hasNewNotification(volunteerId));
    }

    @Transactional
    public void updateNotificationRead(Long volunteerId) {
        volunteerNotificationRepository.updateBulkRead(volunteerId);
    }
}
