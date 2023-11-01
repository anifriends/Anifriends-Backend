package com.clova.anifriends.domain.volunteer.repository;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmail(VolunteerEmail email);
}
