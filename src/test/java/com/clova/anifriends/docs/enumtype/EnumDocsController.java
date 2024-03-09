package com.clova.anifriends.docs.enumtype;

import com.clova.anifriends.domain.animal.AnimalAge;
import com.clova.anifriends.domain.animal.AnimalSize;
import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.common.EnumType;
import com.clova.anifriends.domain.recruitment.dto.request.KeywordFilter;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/docs/enum")
public class EnumDocsController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> getProvider() {
        return ResponseEntity.ok(getEnumTypes(TestEnum.values()));
    }

    @GetMapping("/animal/active")
    public ResponseEntity<Map<String, String>> getAnimalActive() {
        return ResponseEntity.ok(getEnumTypes(AnimalActive.values()));
    }

    @GetMapping("/animal/gender")
    public ResponseEntity<Map<String, String>> getAnimalGender() {
        return ResponseEntity.ok(getEnumTypes(AnimalGender.values()));
    }

    @GetMapping("/animal/type")
    public ResponseEntity<Map<String, String>> getAnimalType() {
        return ResponseEntity.ok(getEnumTypes(AnimalType.values()));
    }

    @GetMapping("/animal/age")
    public ResponseEntity<Map<String, String>> getAnimalAge() {
        return ResponseEntity.ok(getEnumTypes(AnimalAge.values()));
    }

    @GetMapping("/animal/size")
    public ResponseEntity<Map<String, String>> getAnimalSize() {
        return ResponseEntity.ok(getEnumTypes(AnimalSize.values()));
    }

    @GetMapping("/applicant/status")
    public ResponseEntity<Map<String, String>> getApplicantStatus() {
        return ResponseEntity.ok(getEnumTypes(ApplicantStatus.values()));
    }

    @GetMapping("/recruitment/keyword-filter")
    public ResponseEntity<Map<String, String>> getKeywordFilter() {
        return ResponseEntity.ok(getEnumTypes(KeywordFilter.values()));
    }

    @GetMapping("/volunteer/gender")
    public ResponseEntity<Map<String, String>> getVolunteerGender() {
        return ResponseEntity.ok(getEnumTypes(VolunteerGender.values()));
    }

    private Map<String, String> getEnumTypes(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .collect(Collectors.toMap(EnumType::getName, EnumType::getName));
    }
}
