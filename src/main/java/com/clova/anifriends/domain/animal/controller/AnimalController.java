package com.clova.anifriends.domain.animal.controller;

import com.clova.anifriends.domain.animal.dto.request.FindAnimalsByShelterRequest;
import com.clova.anifriends.domain.animal.dto.request.FindAnimalsByVolunteerRequest;
import com.clova.anifriends.domain.animal.dto.request.RegisterAnimalRequest;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalDetail;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByShelterResponse;
import com.clova.anifriends.domain.animal.dto.response.FindAnimalsByVolunteerResponse;
import com.clova.anifriends.domain.animal.dto.response.RegisterAnimalResponse;
import com.clova.anifriends.domain.animal.service.AnimalService;
import com.clova.anifriends.domain.auth.resolver.LoginUser;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/volunteers/animals")
    public ResponseEntity<FindAnimalsByVolunteerResponse> findAnimalsByVolunteer(
        Pageable pageable,
        @ModelAttribute FindAnimalsByVolunteerRequest findAnimalsByVolunteerRequest
    ) {
        return ResponseEntity.ok(animalService.findAnimalsByVolunteer(
            findAnimalsByVolunteerRequest.type(),
            findAnimalsByVolunteerRequest.active(),
            findAnimalsByVolunteerRequest.isNeutered(),
            findAnimalsByVolunteerRequest.age(),
            findAnimalsByVolunteerRequest.gender(),
            findAnimalsByVolunteerRequest.size(),
            pageable
        ));
    }
}
