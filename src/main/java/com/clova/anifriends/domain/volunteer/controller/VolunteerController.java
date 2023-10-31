package com.clova.anifriends.domain.volunteer.controller;

import com.clova.anifriends.domain.volunteer.dto.request.RegisterVolunteerRequest;
import com.clova.anifriends.domain.volunteer.service.VolunteerService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;
    private static final String BASE_URI = "/api/volunteers/";

    @PostMapping
    public ResponseEntity<Void> registerVolunteer(
        @RequestBody @Valid RegisterVolunteerRequest registerVolunteerRequest
    ) {
        Long registeredVolunteerID = volunteerService.registerVolunteer(registerVolunteerRequest);
        URI location = URI.create(BASE_URI + registeredVolunteerID);
        return ResponseEntity.created(location).build();
    }

}
