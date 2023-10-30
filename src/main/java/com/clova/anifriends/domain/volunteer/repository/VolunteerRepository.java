package com.clova.anifriends.domain.volunteer.repository;

import com.clova.anifriends.domain.volunteer.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

}
