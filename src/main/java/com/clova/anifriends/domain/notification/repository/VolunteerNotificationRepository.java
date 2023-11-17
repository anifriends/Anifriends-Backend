package com.clova.anifriends.domain.notification.repository;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VolunteerNotificationRepository extends JpaRepository<VolunteerNotification, Long> {

    List<VolunteerNotification> findByVolunteer_VolunteerIdOrderByCreatedAtDesc(Long volunteerId);

    @Query("select exists (select v from VolunteerNotification v where v.volunteer.volunteerId = :volunteerId and v.isRead.isRead = false)")
    boolean hasNewNotification(@Param("volunteerId") Long volunteerId);
}
