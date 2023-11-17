package com.clova.anifriends.domain.notification.service;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.dto.FindVolunteerNotificationsResponse;
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

    @Transactional
    public void updateNotificationRead(Long volunteerId) {
        volunteerNotificationRepository.updateBulkRead(volunteerId);
    }
}
