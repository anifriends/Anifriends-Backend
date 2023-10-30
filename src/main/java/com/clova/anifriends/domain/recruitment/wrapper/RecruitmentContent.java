package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RecruitmentContent {

    @Column(name = "content")
    private String content;

    protected RecruitmentContent() {
    }

    public RecruitmentContent(String value) {
        this.content = value;
    }

}
