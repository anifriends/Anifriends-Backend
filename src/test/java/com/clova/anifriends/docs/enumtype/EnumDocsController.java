package com.clova.anifriends.docs.enumtype;

import com.clova.anifriends.domain.common.EnumType;
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

    private Map<String, String> getEnumTypes(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .collect(Collectors.toMap(EnumType::getName, EnumType::getValue));
    }
}
