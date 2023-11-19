package com.clova.anifriends.domain.notification.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.dto.response.FindShelterHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.support.fixture.ShelterNotificationFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ShelterNotificationControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 보호소 알림 조회 api 호출 시")
    void findShelterNotifications() throws Exception {
        // given
        Shelter shelter = ShelterFixture.shelter();
        ShelterNotification shelterNotification = ShelterNotificationFixture.shelterNotification(
            shelter);
        ReflectionTestUtils.setField(shelterNotification, "shelterNotificationId", 1L);
        FindShelterNotificationsResponse findShelterNotificationsResponse = FindShelterNotificationsResponse.from(
            List.of(shelterNotification));
        given(shelterNotificationService.findShelterNotifications(anyLong()))
            .willReturn(findShelterNotificationsResponse);

        // when
        ResultActions result = mockMvc.perform(get("/api/shelters/notifications")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("notifications").type(ARRAY).description("보호소 알림 목록"),
                    fieldWithPath("notifications[].notificationId").type(NUMBER)
                        .description("알림 ID"),
                    fieldWithPath("notifications[].notificationTitle").type(STRING)
                        .description("알림 제목"),
                    fieldWithPath("notifications[].notificationContent").type(STRING)
                        .description("알림 내용"),
                    fieldWithPath("notifications[].notificationIsRead").type(BOOLEAN)
                        .description("알림 읽음 여부"),
                    fieldWithPath("notifications[].notificationType").type(STRING)
                        .description("알림 타입")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호소 새로운 알림 여부 조회 api 호출 시")
    void findShelterHasNewNotification() throws Exception {
        // given
        FindShelterHasNewNotificationResponse findShelterHasNewNotificationResponse = FindShelterHasNewNotificationResponse.from(
            true);
        given(shelterNotificationService.findShelterHasNewNotification(anyLong()))
            .willReturn(findShelterHasNewNotificationResponse);

        // when
        ResultActions result = mockMvc.perform(get("/api/shelters/notifications/read")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("hasNewNotification").type(BOOLEAN).description("새로운 알림 존재 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 보호소 알림 읽음 처리 api 호출 시")
    void updateNotificationRead() throws Exception {
        // given
        // when
        ResultActions result = mockMvc.perform(patch("/api/shelters/notification/read")
            .header(AUTHORIZATION, shelterAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                )
            ));
    }
}
