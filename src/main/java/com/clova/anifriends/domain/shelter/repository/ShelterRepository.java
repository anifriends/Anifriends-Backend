package com.clova.anifriends.domain.shelter.repository;

import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.wrapper.ShelterEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    Optional<Shelter> findByEmail(ShelterEmail email);
}
