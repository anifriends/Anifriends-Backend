package com.clova.anifriends.domain.recruitment.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseIntegrationTest;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

class RecruitmentScheduledTest extends BaseIntegrationTest {

    @SpyBean
    private RecruitmentService recruitmentService;

    @Test
    @DisplayName("autoCloseRecruitment 메서드 실행 시")
    void autoCloseRecruitment() {
        Awaitility.await()
            .atMost(3, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(recruitmentService, atLeastOnce()).autoCloseRecruitment();
            });
    }
}
