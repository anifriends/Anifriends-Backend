package com.clova.anifriends.domain.recruitment.support.fixture;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.shelter.Shelter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.util.ReflectionTestUtils;

public class RecruitmentFixture {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String RECRUITMENT_TITLE = "recruitmentTitle";
    private static final String RECRUITMENT_CONTENT = "recruitmentContent";
    private static final LocalDateTime START_TIME = NOW.plusMonths(1);
    private static final LocalDateTime END_TIME = START_TIME.plusHours(2);
    private static final LocalDateTime DEADLINE = START_TIME.minusDays(1);
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

    public static List<Recruitment> recruitments(Shelter shelter, int end) {
        return IntStream.range(0, end)
            .mapToObj(i -> recruitment(shelter))
            .toList();
    }

    public static Recruitment recruitment(Shelter shelter, int capacity) {
        return new Recruitment(
            shelter,
            RECRUITMENT_TITLE,
            capacity,
            RECRUITMENT_CONTENT,
            START_TIME,
            END_TIME,
            DEADLINE,
            IMAGE_URL_LIST
        );
    }

    public static Recruitment recruitmentWithImages(Shelter shelter, List<String> imageUrls) {
        return new Recruitment(
            shelter,
            RECRUITMENT_TITLE,
            CAPACITY,
            RECRUITMENT_CONTENT,
            START_TIME,
            END_TIME,
            DEADLINE,
            imageUrls
        );
    }

    public static List<Recruitment> createRecruitments(List<Shelter> shelters) {
        return shelters.stream()
            .map(RecruitmentFixture::recruitment)
            .toList();
    }

    public static Recruitment recruitment(Shelter shelter, List<Applicant> applicants) {
        Recruitment recruitment = recruitment(shelter);
        ReflectionTestUtils.setField(recruitment, "applicants", applicants);
        return recruitment;
    }

}
