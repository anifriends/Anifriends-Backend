package com.clova.anifriends.base;

import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.config.RedisConfig;
import com.clova.anifriends.global.config.SecurityConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@Import({SecurityConfig.class, RedisConfig.class})
public abstract class BaseIntegrationTest extends TestContainerStarter {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    protected VolunteerRepository volunteerRepository;

    @Autowired
    protected ShelterRepository shelterRepository;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected ApplicantRepository applicantRepository;

    @Autowired
    protected ChatRoomRepository chatRoomRepository;

    @Autowired
    protected ChatMessageRepository chatMessageRepository;

    @Autowired
    protected AnimalRepository animalRepository;

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }
}
