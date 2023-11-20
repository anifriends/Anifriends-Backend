package com.clova.anifriends.global.firebase.service;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.OkHttpClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FCMServiceTest {

    @InjectMocks
    private FCMService fcmService;

    @Mock
    private ObjectMapper objectMapper;

    @MockBean
    private OkHttpClient client;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fcmService, "API_URL",
            "https://fcm.googleapis.com/v1/projects/test/messages:send");
        ReflectionTestUtils.setField(fcmService, "FIREBASE_CONFIG_PATH",
            "backend-config/anifriends-firebase.json");
    }

    @Nested
    @DisplayName("sendMessage 메서드 실행 시")
    class SendMessageTest {

        @Test
        @DisplayName("성공")
        void sendMessage() throws IOException {
            // given
            String deviceToken = "deviceToken";
            String title = "title";
            String content = "content";
            NotificationType notificationType = NotificationType.NEW_APPLICANT;

            when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");

            // when
            Exception exception = catchException(
                () -> fcmService.sendMessage(deviceToken, title, content, notificationType));

            // then
            Assertions.assertThat(exception).isNull();
        }
    }
}
