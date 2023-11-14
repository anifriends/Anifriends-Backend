package com.clova.anifriends.domain.animal.controller;

import com.clova.anifriends.domain.animal.dto.request.FindAnimalsByShelterRequest;
import com.clova.anifriends.domain.animal.dto.request.FindAnimalsRequest;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.request.UpdateAnimalAdoptStatusRequest;
import com.clova.anifriends.domain.animal.dto.request.UpdateAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.service.AnimalService;
import com.clova.anifriends.domain.auth.LoginUser;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/shelters/animals")
    public ResponseEntity<Void> registerAnimal(
        @LoginUser Long userId,
        @RequestBody @Valid RegisterAnimalRequest registerAnimalRequest) {
        RegisterAnimalResponse registerAnimalResponse = animalService.registerAnimal(userId,
            registerAnimalRequest);
        URI location = URI.create("/api/shelters/animals/" + registerAnimalResponse.animalId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/animals/{animalId}")
    public ResponseEntity<FindAnimalDetail> findAnimalDetail(
        @PathVariable Long animalId) {
        return ResponseEntity.ok(animalService.findAnimalDetail(animalId));
    }

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
            findAnimalsByShelterRequest.isNeutered(),
            findAnimalsByShelterRequest.active(),
            findAnimalsByShelterRequest.size(),
            findAnimalsByShelterRequest.age(),
            pageable
        ));
    }

    @GetMapping("/animals")
    public ResponseEntity<FindAnimalsResponse> findAnimals(
        Pageable pageable,
        @ModelAttribute FindAnimalsRequest findAnimalsRequest
    ) {
        return ResponseEntity.ok(animalService.findAnimals(
            findAnimalsRequest.type(),
            findAnimalsRequest.active(),
            findAnimalsRequest.isNeutered(),
            findAnimalsRequest.age(),
            findAnimalsRequest.gender(),
            findAnimalsRequest.size(),
            pageable
        ));
    }

    @PatchMapping("/shelters/animals/{animalId}/status")
    public ResponseEntity<Void> updateAnimalAdoptStatus(
        @LoginUser Long shelterId,
        @PathVariable Long animalId,
        @RequestBody UpdateAnimalAdoptStatusRequest request
    ) {
        animalService.updateAnimalAdoptStatus(shelterId, animalId, request.isAdopted());
        return ResponseEntity.noContent().build();
    }

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

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
