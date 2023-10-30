package com.clova.anifriends.domain.shelter.repository;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.ShelterImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterImageRepository extends JpaRepository<ShelterImage, Long> {

    Optional<ShelterImage> findShelterImageByShelter(Shelter shelter);
}
