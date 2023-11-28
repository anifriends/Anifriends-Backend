package com.clova.anifriends.domain.notification.service;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.VolunteerNotificationRepository;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VolunteerNotificationService {

    private final VolunteerNotificationRepository volunteerNotificationRepository;
    private final RecruitmentRepository recruitmentRepository;

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

    @Transactional
    @Scheduled(cron = "${schedules.cron.notification.a-day-before-volunteer}")
    public void notifyADayBeforeVolunteer() {
        LocalDateTime time1 = LocalDateTime.now().plusDays(1).with(LocalTime.of(0, 1));
        LocalDateTime time2 = LocalDateTime.now().plusDays(1).with(LocalTime.of(23, 59));

        List<VolunteerNotification> volunteerNotifications = recruitmentRepository
            .findRecruitmentByStartTime(time1, time2)
            .stream()
            .flatMap(recruitment ->
                recruitment.getApplicants().stream()
                    .map(applicant -> makeNewADayBeforeVolunteerNotification(applicant, recruitment))
            )
            .collect(Collectors.toList());

        volunteerNotificationRepository.saveAll(volunteerNotifications);
    }

    @Transactional
    @Scheduled(cron = "${schedules.cron.notification.three-day-before-volunteer}")
    public void notifyThreeDayBeforeVolunteer() {
        LocalDateTime time1 = LocalDateTime.now().plusDays(3).with(LocalTime.of(0, 1));
        LocalDateTime time2 = LocalDateTime.now().plusDays(3).with(LocalTime.of(23, 59));

        List<VolunteerNotification> volunteerNotifications = recruitmentRepository
            .findRecruitmentByStartTime(time1, time2)
            .stream()
            .flatMap(recruitment ->
                recruitment.getApplicants().stream()
                    .map(applicant -> makeThreeDayBeforeVolunteerNotification(applicant, recruitment))
            )
            .collect(Collectors.toList());

        volunteerNotificationRepository.saveAll(volunteerNotifications);
    }

    private VolunteerNotification makeNewADayBeforeVolunteerNotification(
        Applicant applicant, Recruitment recruitment) {
        return new VolunteerNotification(
            applicant.getVolunteer(),
            recruitment.getTitle(),
            NotificationType.A_DAY_BEFORE_VOLUNTEER.getMessage(),
            NotificationType.A_DAY_BEFORE_VOLUNTEER.getName()
        );
    }

    private VolunteerNotification makeThreeDayBeforeVolunteerNotification(
        Applicant applicant, Recruitment recruitment) {
        return new VolunteerNotification(
            applicant.getVolunteer(),
            recruitment.getTitle(),
            NotificationType.THREE_DAY_BEFORE_VOLUNTEER.getMessage(),
            NotificationType.THREE_DAY_BEFORE_VOLUNTEER.getName()
        );
    }
}
