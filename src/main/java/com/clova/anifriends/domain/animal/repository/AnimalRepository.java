package com.clova.anifriends.domain.animal.repository;

import com.clova.anifriends.domain.animal.Animal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long>, AnimalRepositoryCustom {

    @Query("select a from Animal a"
        + " join fetch a.images"
        + " where a.animalId = :animalId and a.shelter.shelterId = :shelterId")
    Optional<Animal> findByAnimalIdAndShelterIdWithImages(
        @Param("animalId") Long animalId,
        @Param("shelterId") Long shelterId);
}
