package com.clova.anifriends.base;

import com.clova.anifriends.base.config.TestQueryDslConfig;
import com.clova.anifriends.domain.animal.repository.AnimalRepository;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.repository.VolunteerNotificationRepository;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.repository.ReviewRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.config.JpaAuditingConfig;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestQueryDslConfig.class, JpaAuditingConfig.class})
public abstract class BaseRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected AnimalRepository animalRepository;

    @Autowired
    protected ApplicantRepository applicantRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected ShelterRepository shelterRepository;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;

    @Autowired
    protected VolunteerRepository volunteerRepository;

    @Autowired
    protected ChatMessageRepository chatMessageRepository;

    @Autowired
    protected ChatRoomRepository chatRoomRepository;

    @Autowired
    protected ShelterNotificationRepository shelterNotificationRepository;

    @Autowired
    protected VolunteerNotificationRepository volunteerNotificationRepository;
}
