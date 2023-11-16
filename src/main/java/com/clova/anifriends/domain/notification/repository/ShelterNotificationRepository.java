package com.clova.anifriends.domain.notification.repository;

import com.clova.anifriends.domain.notification.ShelterNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShelterNotificationRepository extends JpaRepository<ShelterNotification, Long> {

    List<ShelterNotification> findByShelter_ShelterIdOrderByCreatedAtDesc(Long shelterId);

    @Query("select exists (select s from ShelterNotification s where s.shelter.shelterId = :shelterId and s.isRead.isRead = false)")
    boolean hasNewNotification(@Param("shelterId") Long shelterId);
}
