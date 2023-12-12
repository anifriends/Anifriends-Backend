package com.clova.anifriends.global.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.springframework.http.HttpMethod.POST;

import com.clova.anifriends.domain.payment.dto.response.TossPaymentApiResponse;
import com.clova.anifriends.global.exception.ExternalApiException;
import java.io.IOException;
import net.minidev.json.JSONObject;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

class ApiServiceTest {

    private ApiService apiService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        apiService = new ApiService(WebClient.create());
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("post 실행 ")
    class PostTest {

        @Test
        @DisplayName("성공: 외부 api 에 post 요청하여 응답을 받는다")
        void post() throws InterruptedException {
            // given
            String expectedJsonResponse = "{\"status\": \"DONE\"}";
            String url = mockWebServer.url("/").toString();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject params = new JSONObject();
            params.put("paymentKey", "mockPaymentKey");
            params.put("orderId", "mockOrderId");
            params.put("amount", 10000);

            mockWebServer.enqueue(
                new MockResponse()
                    .addHeader("Content-Type", "application/json")
                    .setResponseCode(200)
                    .setBody(expectedJsonResponse)
            );

            // when
            TossPaymentApiResponse response = apiService.post(
                new HttpEntity<>(params, httpHeaders),
                url,
                TossPaymentApiResponse.class);

            // then
            RecordedRequest request = mockWebServer.takeRequest();
            assertThat(request.getMethod()).isEqualTo(POST.name());
            assertThat(request.getHeader("Content-Type")).isEqualTo("application/json");
            assertThat(request.getBody().readUtf8()).isEqualTo(params.toJSONString());

            assertThat(response).isNotNull();
            assertThat(response.status()).isEqualTo("DONE");
        }

        @Test
        @DisplayName("예외(ExternalApiException): 상태 코드가 4xx 인 경우")
        void exceptionWhenResponseIsNull() {
            // given
            String expectedJsonResponse = "{\"status\": \"DONE\"}";
            String url = mockWebServer.url("/").toString();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject params = new JSONObject();
            params.put("paymentKey", "mockPaymentKey");
            params.put("orderId", "mockOrderId");
            params.put("amount", 10000);

            mockWebServer.enqueue(
                new MockResponse()
                    .addHeader("Content-Type", "application/json")
                    .setResponseCode(401)
                    .setBody(expectedJsonResponse)
            );

            // when
            Exception exception = catchException(() -> apiService.post(
                new HttpEntity<>(params, httpHeaders),
                url,
                TossPaymentApiResponse.class));

            // then
            assertThat(exception).isInstanceOf(ExternalApiException.class);
        }
    }
}
