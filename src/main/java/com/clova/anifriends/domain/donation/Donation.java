package com.clova.anifriends.domain.donation;


import static java.util.Objects.isNull;

import com.clova.anifriends.domain.common.BaseTimeEntity;
import com.clova.anifriends.domain.donation.vo.DonationAmount;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "donation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation extends BaseTimeEntity {

    @Id
    @Column(name = "donation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Embedded
    private DonationAmount amount;

    public Donation(Shelter shelter, Volunteer volunteer, Integer amount) {
        validateShelter(shelter);
        validateVolunteer(volunteer);

        this.shelter = shelter;
        this.volunteer = volunteer;
        this.amount = new DonationAmount(amount);
    }

    private void validateShelter(Shelter shelter) {
        if (isNull(shelter)) {
            throw new ShelterNotFoundException("보호소가 존재하지 않습니다.");
        }
    }

    private void validateVolunteer(Volunteer volunteer) {
        if (isNull(volunteer)) {
            throw new VolunteerNotFoundException("봉사자가 존재하지 않습니다.");
        }
    }

    public int getAmount() {
        return amount.getAmount();
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public Shelter getShelter() {
        return shelter;
    }
}
