package com.clova.anifriends.domain.donation.support.fixture;

import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;

public class DonationFixture {

    public static final int AMOUNT = 1000;

    public static Donation donation(Shelter shelter, Volunteer volunteer) {
        return new Donation(shelter, volunteer, AMOUNT);
    }

}
