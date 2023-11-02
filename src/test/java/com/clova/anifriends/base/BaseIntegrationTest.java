package com.clova.anifriends.base;

import jakarta.persistence.EntityManager;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }
}
