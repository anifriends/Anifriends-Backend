package com.clova.anifriends.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.exception.DonationNotFoundException;
import com.clova.anifriends.domain.donation.support.fixture.DonationFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.shelter.vo.ShelterName;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PaymentTest {

    @Nested
    @DisplayName("Payment 생성 시")
    class NewPayment {

        @Test
        @DisplayName("성공")
        void newPayment() {
            // given
            String shelterName = "마석 보호소";
            Shelter shelter = ShelterFixture.shelter();
            ReflectionTestUtils.setField(shelter, "name", new ShelterName(shelterName));
            Volunteer volunteer = VolunteerFixture.volunteer();
            Donation donation = DonationFixture.donation(shelter, volunteer);

            // when
            Payment payment = new Payment(donation);

            // then
            assertThat(payment.getDonation()).isEqualTo(donation);
            assertThat(payment.getOrderId()).isInstanceOf(UUID.class);
            assertThat(payment.getOrderName()).isEqualTo(shelterName + " 후원금");
        }

        @Test
        @DisplayName("예외(DonationNotFoundException): 후원 정보가 존재하지 않을 경우")
        void exceptionWhenDonationNotExist() {
            // given
            Donation nullDonation = null;

            // when
            Exception exception = catchException(() -> new Payment(nullDonation));

            // then
            assertThat(exception).isInstanceOf(DonationNotFoundException.class);
        }
    }

}