package com.clova.anifriends.base;

import com.clova.anifriends.base.config.TestQueryDslConfig;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestQueryDslConfig.class)
public abstract class BaseRepositoryTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected ShelterRepository shelterRepository;

    @Autowired
    protected RecruitmentRepository recruitmentRepository;
}
