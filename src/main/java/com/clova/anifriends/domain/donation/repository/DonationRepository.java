package com.clova.anifriends.domain.donation.repository;

import com.clova.anifriends.domain.donation.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {

}
