package com.clova.anifriends.domain.recruitment.wrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RecruitmentTitle {

    @Column(name = "title")
    private String title;

    protected RecruitmentTitle() {
    }

    public RecruitmentTitle(String value) {
        this.title = value;
    }

}
