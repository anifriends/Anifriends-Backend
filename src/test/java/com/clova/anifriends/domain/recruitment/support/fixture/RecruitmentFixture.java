package com.clova.anifriends.domain.recruitment.support.fixture;

import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDateTime;
import java.util.List;

public class RecruitmentFixture {

    private static final String RECRUITMENT_TITLE = "recruitmentTitle";
    private static final String RECRUITMENT_CONTENT = "recruitmentContent";
    private static final LocalDateTime START_TIME = LocalDateTime.parse("2023-10-31T15:00:00.000");
    private static final LocalDateTime END_TIME = LocalDateTime.parse("2023-10-31T17:00:00.000");
    private static final LocalDateTime DEADLINE = LocalDateTime.parse("2023-10-30T00:00:00.000");
    private static final int CAPACITY = 10;
    private static final List<String> IMAGE_URL_LIST = List.of("imageUrl1", "imageUrl2");

    public static Recruitment recruitment(Shelter shelter) {
        return new Recruitment(
            shelter,
            RECRUITMENT_TITLE,
            CAPACITY,
            RECRUITMENT_CONTENT,
            START_TIME,
            END_TIME,
            DEADLINE,
            IMAGE_URL_LIST
        );
    }

}
