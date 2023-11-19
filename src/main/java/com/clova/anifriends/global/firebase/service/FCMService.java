package com.clova.anifriends.global.firebase.service;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import com.clova.anifriends.global.firebase.domain.FCMMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FCMService {

    @Value("${firebase.api-url}")
    private String API_URL;

    @Value("${firebase.config-path}")
    private String FIREBASE_CONFIG_PATH;

    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Response sendMessage(String deviceToken, String title, String content,
        NotificationType type) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FCMMessage message = FCMMessage.makeMessage(deviceToken, title, content, type);
        RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(message),
            MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
            .url(API_URL)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build();

        return client.newCall(request).execute();
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
            .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())
            .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
