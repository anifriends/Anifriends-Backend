package com.clova.anifriends.global.scheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.clova.anifriends.base.BaseSchedulerTest;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.Awaitility;

class ServiceSchedulerTest extends BaseSchedulerTest {

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
