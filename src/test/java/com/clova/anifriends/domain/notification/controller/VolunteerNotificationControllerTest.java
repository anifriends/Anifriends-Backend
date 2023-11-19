package com.clova.anifriends.domain.notification.controller;

import static java.sql.JDBCType.ARRAY;
import static java.sql.JDBCType.BOOLEAN;
import static javax.management.openmbean.SimpleType.STRING;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerNotificationsResponse;
import com.clova.anifriends.domain.notification.support.fixture.VolunteerNotificationFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class VolunteerNotificationControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 봉사자 알림 조회 api 호출 시")
    void findVolunteerNotifications() throws Exception {
        // given
        Volunteer volunteer = VolunteerFixture.volunteer();
        VolunteerNotification volunteerNotification = VolunteerNotificationFixture.volunteerNotification(
            volunteer);
        ReflectionTestUtils.setField(volunteerNotification, "volunteerNotificationId", 1L);
        FindVolunteerNotificationsResponse findVolunteerNotificationsResponse = FindVolunteerNotificationsResponse.from(
            List.of((volunteerNotification)));
        given(volunteerNotificationService.findVolunteerNotifications(anyLong()))
            .willReturn(findVolunteerNotificationsResponse);

        // when
        ResultActions result = mockMvc.perform(get("/api/volunteers/notifications")
            .header(AUTHORIZATION, volunteerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
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
    @DisplayName("성공: 봉사자 새로운 알림 여부 조회 api 호출 시")
    void findVolunteerHasNewNotification() throws Exception {
        // given
        FindVolunteerHasNewNotificationResponse findVolunteerHasNewNotificationResponse = FindVolunteerHasNewNotificationResponse.from(
            true);
        given(volunteerNotificationService.findVolunteerHasNewNotification(anyLong()))
            .willReturn(findVolunteerHasNewNotificationResponse);

        // when
        ResultActions result = mockMvc.perform(get("/api/volunteers/notifications/read")
            .header(AUTHORIZATION, volunteerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("hasNewNotification").type(JsonFieldType.BOOLEAN).description("새로운 알림 존재 여부")
                )
            ));
    }

    @Test
    @DisplayName("성공: 봉사자 알림 읽음 처리 api 호출 시")
    void updateNotificationRead() throws Exception {
        // given
        // when
        ResultActions result = mockMvc.perform(patch("/api/volunteers/notifications/read")
            .header(AUTHORIZATION, volunteerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNoContent())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                )
            ));
    }
}
