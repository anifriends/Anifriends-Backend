package com.clova.anifriends.domain.notification.service;

import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.dto.response.FindShelterHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShelterNotificationService {

    private final ShelterNotificationRepository shelterNotificationRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    public FindShelterNotificationsResponse findShelterNotifications(Long shelterId) {
        List<ShelterNotification> shelterNotifications = shelterNotificationRepository.findByShelter_ShelterIdOrderByCreatedAtDesc(
            shelterId);
        return FindShelterNotificationsResponse.from(shelterNotifications);
    }

    @Transactional(readOnly = true)
    public FindShelterHasNewNotificationResponse findShelterHasNewNotification(Long shelterId) {
        return FindShelterHasNewNotificationResponse.from(
            shelterNotificationRepository.hasNewNotification(shelterId));
    }

    @Transactional
    public void updateNotificationRead(Long shelterId) {
        shelterNotificationRepository.updateBulkRead(shelterId);
    }

    @Transactional
    public void notifyEncourageCheckAttendance() {
        LocalDateTime time1 = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime time2 = LocalDateTime.now().withMinute(59).withSecond(59)
            .withNano(999_999_999);

        List<ShelterNotification> shelterNotifications = recruitmentRepository
            .findRecruitmentsByStartTime(time1, time2)
            .stream()
            .flatMap(recruitment ->
                recruitment.getApplicants().stream()
                    .map(
                        applicant -> makeNewEncourageAttendanceNotification(recruitment))
            )
            .toList();

        shelterNotificationRepository.saveAll(shelterNotifications);
    }

    private ShelterNotification makeNewEncourageAttendanceNotification(Recruitment recruitment) {
        return new ShelterNotification(
            recruitment.getShelter(),
            recruitment.getTitle(),
            NotificationType.ENCOURAGE_CHECK_ATTENDANCE.getMessage(),
            NotificationType.ENCOURAGE_CHECK_ATTENDANCE.getName()
        );
    }
}
