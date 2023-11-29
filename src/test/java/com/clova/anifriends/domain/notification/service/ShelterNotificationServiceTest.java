package com.clova.anifriends.domain.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.support.ApplicantFixture;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.dto.response.FindShelterHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.support.fixture.ShelterNotificationFixture;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.recruitment.support.fixture.RecruitmentFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ShelterNotificationServiceTest {

    @InjectMocks
    ShelterNotificationService shelterNotificationService;

    @Mock
    ShelterNotificationRepository shelterNotificationRepository;

    @Mock
    RecruitmentRepository recruitmentRepository;

    @Nested
    @DisplayName("findShelterNotifications 메서드 실행 시")
    class FindShelterNotificationsTest {

        @Test
        @DisplayName("성공")
        void findShelterNotifications() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);
            ShelterNotification shelterNotification = ShelterNotificationFixture.shelterNotification(
                shelter);
            FindShelterNotificationsResponse expected = FindShelterNotificationsResponse.from(
                List.of(shelterNotification));
            given(shelterNotificationRepository.findByShelter_ShelterIdOrderByCreatedAtDesc(
                anyLong()))
                .willReturn(List.of(shelterNotification));

            // when
            FindShelterNotificationsResponse result = shelterNotificationService.findShelterNotifications(
                shelter.getShelterId());

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("findShelterHasNewNotification 메서드 실행 시")
    class FindShelterHasNewNotificationTest {

        @Test
        @DisplayName("성공")
        void findShelterHasNewNotification() {
            // given
            FindShelterHasNewNotificationResponse expected = FindShelterHasNewNotificationResponse.from(
                true);
            given(shelterNotificationRepository.hasNewNotification(anyLong())).willReturn(true);

            // when
            FindShelterHasNewNotificationResponse result = shelterNotificationService.findShelterHasNewNotification(
                1L);

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("updateNotificationRead 메서드 실행 시")
    class UpdateNotificationReadTest {

        @Test
        @DisplayName("성공")
        void updateNotificationRead() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);

            // when
            shelterNotificationService.updateNotificationRead(shelter.getShelterId());

            // then
            verify(shelterNotificationRepository, times(1))
                .updateBulkRead(shelter.getShelterId());
        }
    }

    @Nested
    @DisplayName("notifyEncourageCheckAttendance 메서드 실행 시")
    class NotifyEncourageCheckAttendanceTest {

        @Test
        @DisplayName("성공")
        void notifyEncourageCheckAttendance() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Recruitment recruitment = RecruitmentFixture.recruitment(shelter);
            Volunteer volunteer = VolunteerFixture.volunteer();
            Applicant applicant = ApplicantFixture.applicant(recruitment, volunteer);
            given(recruitmentRepository.findRecruitmentsByStartTime(any(), any()))
                .willReturn(List.of(recruitment));

            // when
            shelterNotificationService.notifyEncourageCheckAttendance();

            // then
            verify(recruitmentRepository, times(1))
                .findRecruitmentsByStartTime(any(), any());
            verify(shelterNotificationRepository, times(1))
                .saveAll(any());
        }
    }
}
