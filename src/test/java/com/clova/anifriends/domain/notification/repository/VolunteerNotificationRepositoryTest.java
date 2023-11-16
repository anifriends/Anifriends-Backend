package com.clova.anifriends.domain.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.support.fixture.VolunteerNotificationFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class VolunteerNotificationRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findByVolunteer_VolunteerIdOrderByCreatedAtDesc 실행 시")
    class FindByVolunteer_VolunteerIdOrderByCreatedAtDescTest {

        @Test
        @DisplayName("성공: 생성일 내림차순 조회")
        void findByVolunteer_VolunteerIdOrderByCreatedAtDesc() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            VolunteerNotification notification1 = VolunteerNotificationFixture.volunteerNotification(volunteer);
            VolunteerNotification notification2 = VolunteerNotificationFixture.volunteerNotification(volunteer);
            ReflectionTestUtils.setField(notification1, "createdAt", LocalDateTime.now().minusDays(3));
            ReflectionTestUtils.setField(notification2, "createdAt", LocalDateTime.now().minusDays(1));

            volunteerRepository.save(volunteer);
            volunteerNotificationRepository.save(notification1);
            volunteerNotificationRepository.save(notification2);

            // when
            List<VolunteerNotification> expected = volunteerNotificationRepository.findByVolunteer_VolunteerIdOrderByCreatedAtDesc(
                volunteer.getVolunteerId());

            // then
            assertThat(expected.get(0)).isEqualTo(notification2);
            assertThat(expected.get(1)).isEqualTo(notification1);
        }
    }
}
