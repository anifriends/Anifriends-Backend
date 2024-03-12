package com.clova.anifriends.base;

import com.clova.anifriends.domain.animal.repository.AnimalRedisRepository;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.donation.repository.DonationRepository;
import com.clova.anifriends.domain.payment.repository.PaymentRepository;
import com.clova.anifriends.domain.payment.service.PaymentClient;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.config.RedisConfig;
import com.clova.anifriends.global.config.SecurityConfig;
import com.clova.anifriends.global.image.S3Service;
import com.clova.anifriends.global.infrastructure.ApiService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
@Import({SecurityConfig.class, RedisConfig.class})
public abstract class BaseIntegrationTest extends TestContainerStarter {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @Autowired
    protected ApplicationEvents events;

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

    @Autowired
    protected AnimalRedisRepository animalRedisRepository;

    @Autowired
    protected DonationRepository donationRepository;

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected PaymentClient paymentClient;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @MockBean
    protected ApiService apiService;

    @MockBean
    protected S3Service s3Service;

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }
}
