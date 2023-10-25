package com.clova.anifriends.docs.format;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/docs")
public class DocumentationTestController {

    @GetMapping("/format/optional-param")
    public void formatParam(
        @RequestParam String required,
        @RequestParam(required = false) String optional) {
    }

    @GetMapping("/format/optional-body")
    public void formatBody(@RequestBody TestRequest testRequest) {
    }
}
