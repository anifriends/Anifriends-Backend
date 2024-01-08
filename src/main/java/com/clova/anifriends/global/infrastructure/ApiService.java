package com.clova.anifriends.global.infrastructure;

import static java.util.Objects.requireNonNull;

import com.clova.anifriends.global.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final WebClient webClient;

    public <T> T post(HttpEntity httpEntity, String url, Class<T> clazz) {
        try {
            ResponseEntity<T> response = webClient
                .post()
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(httpEntity.getBody()))
                .retrieve()
                .toEntity(clazz)
                .block();

            return requireNonNull(response).getBody();
        } catch (Exception exception) {
            throw new ExternalApiException("외부 API 호출 과정에서 오류가 발생했습니다." + exception.getMessage());
        }
    }
}
