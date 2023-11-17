package com.clova.anifriends.base;

import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.config.SecurityConfig;
import jakarta.persistence.EntityManager;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@Import({SecurityConfig.class})
public abstract class BaseIntegrationTest {

    @BeforeAll
    static void beforeAll() {
        Properties properties = System.getProperties();
        properties.setProperty("ACCESS_TOKEN_SECRET", "_4RNpxi%CB:eoO6a>j=#|*e#$Fp%%aX{dFi%.!Y(ZIy'UMuAt.9.;LxpWn2BZV*");
        properties.setProperty("REFRESH_TOKEN_SECRET", "Tlolt.z[e$1yO!%Uc\"F*QH=uf0vp3U5s5{X5=g=*nDZ>BWMIKIf9nzd6et2.:Fb");
    }

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

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }
}
