package com.clova.anifriends.domain.donation.repository;

public interface DonationCacheRepository {

    boolean isDuplicateDonation(Long volunteerId);
}
