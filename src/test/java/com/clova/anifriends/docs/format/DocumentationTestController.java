package com.clova.anifriends.docs.format;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/format/date")
    public String date(@RequestParam LocalDate date) {
        return date.toString();
    }

    @GetMapping("/format/datetime")
    public String datetime(@RequestParam LocalDateTime datetime) {
        return datetime.toString();
    }
}
