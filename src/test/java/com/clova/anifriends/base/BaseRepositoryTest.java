package com.clova.anifriends.base;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public abstract class BaseRepositoryTest {

    @Autowired
    protected EntityManager entityManager;
}
