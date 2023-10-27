package com.clova.anifriends.docs.format;

import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.snippet.Attributes;

public interface DocumentationFormatGenerator {

    static Attributes.Attribute getDateConstraint() {
        return key("constraint").value("yyyy-MM-dd");
    }

    static Attributes.Attribute getDatetimeConstraint() {
        return key("constraint").value("yyyy-MM-dd'T'HH:mm:ss");
    }

    static Attributes.Attribute getConstraint(String value) {
        return key("constraint").value(value);
    }
}
