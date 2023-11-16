package com.clova.anifriends.domain.chat.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.chat.dto.request.RegisterChatRoomRequest;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ChatRoomControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 채팅방 생성 api 호출")
    void registerChatRoom() throws Exception {
        // given
        Volunteer volunteer = VolunteerFixture.volunteer();
        ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", 1L);

        RegisterChatRoomRequest request = new RegisterChatRoomRequest(
            shelter.getShelterId());

        long chatRoomId = 1;

        when(chatService.registerChatRoom(volunteer.getVolunteerId(), shelter.getShelterId()))
            .thenReturn(chatRoomId);

        // when
        ResultActions result = mockMvc.perform(post("/api/volunteers/chat/rooms")
            .header(AUTHORIZATION, volunteerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("shelterId").type(NUMBER).description("보호소 ID")
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 채팅방의 URI")
                )
            ));
    }
}
