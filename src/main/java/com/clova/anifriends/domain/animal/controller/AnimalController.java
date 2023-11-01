package com.clova.anifriends.domain.animal.controller;

import com.clova.anifriends.domain.animal.dto.response.FindAnimalByVolunteerResponse;
import com.clova.anifriends.domain.animal.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping("volunteers/animals/{animalId}")
    public ResponseEntity<FindAnimalByVolunteerResponse> findAnimalByIdByVolunteer(
        @PathVariable Long animalId) {
        return ResponseEntity.ok(animalService.findAnimalByIdByVolunteer(animalId));
    }
}
