package com.clova.anifriends.domain.animal.controller;

import com.clova.anifriends.domain.animal.dto.FindAnimalsByVolunteerRequestV2;
import com.clova.anifriends.domain.animal.dto.request.FindAnimalsByShelterRequest;
import com.clova.anifriends.domain.animal.dto.request.FindAnimalsByVolunteerRequest;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.request.UpdateAnimalAdoptStatusRequest;
import com.clova.anifriends.domain.animal.dto.request.UpdateAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.service.AnimalService;
import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.auth.authorization.ShelterOnly;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @ShelterOnly
    @PostMapping("/shelters/animals")
    public ResponseEntity<RegisterAnimalResponse> registerAnimal(
        @LoginUser Long volunteerId,
        @RequestBody @Valid RegisterAnimalRequest registerAnimalRequest) {
        RegisterAnimalResponse registerAnimalResponse = animalService.registerAnimal(volunteerId,
            registerAnimalRequest);
        URI location = URI.create("/api/shelters/animals/" + registerAnimalResponse.animalId());
        return ResponseEntity.created(location).body(registerAnimalResponse);
    }

    @GetMapping("/animals/{animalId}")
    public ResponseEntity<FindAnimalDetail> findAnimalDetail(
        @PathVariable Long animalId) {
        return ResponseEntity.ok(animalService.findAnimalDetail(animalId));
    }

    @ShelterOnly
    @GetMapping("/shelters/animals")
    public ResponseEntity<FindAnimalsByShelterResponse> findAnimalsByShelter(
        @LoginUser Long shelterId,
        @ModelAttribute @Valid FindAnimalsByShelterRequest findAnimalsByShelterRequest,
        Pageable pageable
    ) {
        return ResponseEntity.ok(animalService.findAnimalsByShelter(
            shelterId,
            findAnimalsByShelterRequest.keyword(),
            findAnimalsByShelterRequest.type(),
            findAnimalsByShelterRequest.gender(),
            findAnimalsByShelterRequest.neuteredFilter(),
            findAnimalsByShelterRequest.active(),
            findAnimalsByShelterRequest.animalSize(),
            findAnimalsByShelterRequest.age(),
            pageable
        ));
    }

    @GetMapping("/animals")
    public ResponseEntity<FindAnimalsResponse> findAnimalsByVolunteer(
        Pageable pageable,
        @ModelAttribute FindAnimalsByVolunteerRequest findAnimalsRequest
    ) {
        return ResponseEntity.ok(animalService.findAnimalsByVolunteer(
            findAnimalsRequest.type(),
            findAnimalsRequest.active(),
            findAnimalsRequest.neuteredFilter(),
            findAnimalsRequest.age(),
            findAnimalsRequest.gender(),
            findAnimalsRequest.animalSize(),
            pageable
        ));
    }

    @GetMapping("/v2/animals")
    public ResponseEntity<FindAnimalsResponse> findAnimalsByVolunteerV2(
        Pageable pageable,
        @ModelAttribute FindAnimalsByVolunteerRequestV2 findAnimalsByVolunteerRequestV2
    ) {
        return ResponseEntity.ok(animalService.findAnimalsByVolunteerV2(
            findAnimalsByVolunteerRequestV2.type(),
            findAnimalsByVolunteerRequestV2.active(),
            findAnimalsByVolunteerRequestV2.neuteredFilter(),
            findAnimalsByVolunteerRequestV2.age(),
            findAnimalsByVolunteerRequestV2.gender(),
            findAnimalsByVolunteerRequestV2.animalSize(),
            findAnimalsByVolunteerRequestV2.createdAt(),
            findAnimalsByVolunteerRequestV2.animalId(),
            pageable
        ));
    }

    @ShelterOnly
    @PatchMapping("/shelters/animals/{animalId}/status")
    public ResponseEntity<Void> updateAnimalAdoptStatus(
        @LoginUser Long shelterId,
        @PathVariable Long animalId,
        @RequestBody UpdateAnimalAdoptStatusRequest request
    ) {
        animalService.updateAnimalAdoptStatus(shelterId, animalId, request.isAdopted());
        return ResponseEntity.noContent().build();
    }

    @ShelterOnly
    @PatchMapping("/shelters/animals/{animalId}")
    public ResponseEntity<Void> updateAnimal(
        @LoginUser Long shelterId,
        @PathVariable Long animalId,
        @RequestBody @Valid UpdateAnimalRequest updateAnimalRequest
    ) {
        animalService.updateAnimal(
            shelterId,
            animalId,
            updateAnimalRequest.name(),
            updateAnimalRequest.birthDate(),
            updateAnimalRequest.type(),
            updateAnimalRequest.breed(),
            updateAnimalRequest.gender(),
            updateAnimalRequest.isNeutered(),
            updateAnimalRequest.active(),
            updateAnimalRequest.weight(),
            updateAnimalRequest.information(),
            updateAnimalRequest.imageUrls()
        );

        return ResponseEntity.noContent().build();
    }

    @ShelterOnly
    @DeleteMapping("/shelters/animals/{animalId}")
    public ResponseEntity<Void> deleteAnimal(
        @LoginUser Long shelterId,
        @PathVariable Long animalId) {
        animalService.deleteAnimal(shelterId, animalId);
        return ResponseEntity.noContent().build();
    }
}
