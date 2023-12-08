package com.clova.anifriends.domain.donation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DonationTest {

    @Nested
    @DisplayName("Donation 생성 시")
    class NewDonation {

        @Test
        @DisplayName("성공")
        void newDonation() {
            //given
            Integer amount = 1000;
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();

            //when
            Donation donation = new Donation(shelter, volunteer, amount);

            //then
            assertThat(donation.getAmount()).isEqualTo(amount);
            assertThat(donation.getShelter()).isEqualTo(shelter);
            assertThat(donation.getVolunteer()).isEqualTo(volunteer);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 보호소가 존재하지 않을 경우")
        void exceptionWhenShelterNotExist() {
            //given
            Integer amount = 1000;
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter nullShelter = null;

            //when
            Exception exception = catchException(
                () -> new Donation(nullShelter, volunteer, amount));

            //then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }

        @Test
        @DisplayName("예외(VolunteerNotFoundException): 봉사자가 존재하지 않을 경우")
        void exceptionWhenVolunteerNotExist() {
            //given
            Integer amount = 1000;
            Volunteer nullVolunteer = null;
            Shelter shelter = ShelterFixture.shelter();

            //when
            Exception exception = catchException(
                () -> new Donation(shelter, nullVolunteer, amount));

            //then
            assertThat(exception).isInstanceOf(VolunteerNotFoundException.class);
        }

    }

}