package com.clova.anifriends.domain.volunteer.repository;

import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.VolunteerImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerImageRepository extends JpaRepository<VolunteerImage, Long> {

    Optional<VolunteerImage> findByVolunteer(Volunteer volunteer);
}
