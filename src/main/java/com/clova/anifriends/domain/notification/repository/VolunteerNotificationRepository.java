package com.clova.anifriends.domain.notification.repository;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerNotificationRepository extends JpaRepository<VolunteerNotification, Long> {

    List<VolunteerNotification> findByVolunteer_VolunteerIdOrderByCreatedAtDesc(Long volunteerId);
}
