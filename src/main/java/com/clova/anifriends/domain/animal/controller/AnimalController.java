package com.clova.anifriends.domain.animal.controller;

import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.service.AnimalService;
import com.clova.anifriends.domain.auth.resolver.LoginUser;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("volunteers/animals/{animalId}")
    public ResponseEntity<FindAnimalByVolunteerResponse> findAnimalByIdByVolunteer(
        @PathVariable Long animalId) {
        return ResponseEntity.ok(animalService.findAnimalByIdByVolunteer(animalId));
    }

    @GetMapping("/shelters/animals/{animalId}")
    public ResponseEntity<FindAnimalByShelterResponse> findAnimalByShelter(
        @LoginUser Long shelterId,
        @PathVariable Long animalId) {
        return ResponseEntity.ok(animalService.findAnimalByShelter(animalId, shelterId));
    }
}
