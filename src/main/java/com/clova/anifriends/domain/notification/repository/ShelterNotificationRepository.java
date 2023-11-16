package com.clova.anifriends.domain.notification.repository;

import com.clova.anifriends.domain.notification.ShelterNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterNotificationRepository extends JpaRepository<ShelterNotification, Long> {

    List<ShelterNotification> findByShelter_ShelterIdOrderByCreatedAtDesc(Long shelterId);
}
