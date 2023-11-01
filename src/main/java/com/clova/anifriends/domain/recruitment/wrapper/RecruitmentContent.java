package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentContent {

    @Column(name = "content")
    private String content;

    public RecruitmentContent(String value) {
        this.content = value;
    }
}
