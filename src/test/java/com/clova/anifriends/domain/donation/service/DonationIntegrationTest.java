package com.clova.anifriends.domain.donation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.awaitility.Awaitility.await;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.donation.exception.DonationDuplicateException;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DonationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DonationService donationService;

    @Nested
    @DisplayName("registerDonation 실행 시")
    class RegisterDonationTest {

        @Test
        @DisplayName("성공: 1초 간격으로 두 번 후원 요청 시")
        void registerDonation() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            int amount = 100_000;

            volunteerRepository.save(volunteer);
            shelterRepository.save(shelter);

            // when && then
            donationService.registerDonation(volunteer.getVolunteerId(), shelter.getShelterId(),
                amount);

            await().pollDelay(Duration.ofSeconds(1)).untilAsserted(() -> {
                assertThat(catchException(
                    () -> donationService.registerDonation(volunteer.getVolunteerId(),
                        shelter.getShelterId(), amount))).isNull();
            });
        }

        @Test
        @DisplayName("예외(DonationDuplicateException): 1초 안에 연속 두 번 후원 요청 시")
        void exceptionWhenDuplicateDonation() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            int amount = 100_000;

            volunteerRepository.save(volunteer);
            shelterRepository.save(shelter);

            // when
            Exception exception = catchException(() -> {
                donationService.registerDonation(volunteer.getVolunteerId(), shelter.getShelterId(),
                    amount);
                donationService.registerDonation(volunteer.getVolunteerId(), shelter.getShelterId(),
                    amount);
            });

            // then
            assertThat(exception).isInstanceOf(DonationDuplicateException.class);
        }

    }

}
