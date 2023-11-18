package com.clova.anifriends.domain.chat.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.request.RegisterChatRoomRequest;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomIdResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse.FindChatRoomResponse;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class ChatRoomControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 채팅방 목록 조회(봉사자) api 호출 시")
    void findChatRoomsByVolunteer() throws Exception {
        //given
        Long chatRoomId = 1L;
        String chatRecentMessage = "최근 메시지";
        String chatPartnerName = "채팅 상대방 이름";
        String chatPartnerImageUrl = "채팅 상대방 이미지 url";
        LocalDateTime createdAt = LocalDateTime.now();
        Long chatUnReadCount = 5L;
        FindChatRoomResponse findChatRoomResponse = new FindChatRoomResponse(chatRoomId,
            chatRecentMessage, chatPartnerName, chatPartnerImageUrl, createdAt, chatUnReadCount);
        FindChatRoomsResponse findChatRoomsResponse = new FindChatRoomsResponse(
            List.of(findChatRoomResponse));

        given(chatRoomService.findChatRoomsByVolunteer(1L)).willReturn(findChatRoomsResponse);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/volunteers/chat/rooms")
            .header(AUTHORIZATION, volunteerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("chatRooms").type(JsonFieldType.ARRAY).description("채팅방 목록"),
                    fieldWithPath("chatRooms[].chatRoomId").type(NUMBER)
                        .description("채팅방 ID"),
                    fieldWithPath("chatRooms[].chatRecentMessage").type(JsonFieldType.STRING)
                        .description("채팅방 최근 메시지"),
                    fieldWithPath("chatRooms[].chatPartnerName").type(JsonFieldType.STRING)
                        .description("채팅방 상대방 이름"),
                    fieldWithPath("chatRooms[].charPartnerImageUrl").type(JsonFieldType.STRING)
                        .description("채팅방 상대방 이미지 URL"),
                    fieldWithPath("chatRooms[].createdAt").type(JsonFieldType.STRING)
                        .description("채팅방 최근 메시지 발송 시간"),
                    fieldWithPath("chatRooms[].chatUnReadCount").type(NUMBER)
                        .description("채팅방 최근 메시지 발송 시간")
                )));
    }

    @Test
    @DisplayName("성공: 채팅방 상세 조회(봉사자) api 호출 시")
    void findChatRoomDetailByVolunteer() throws Exception {
        //given
        Long chatRoomId = 1L;
        Shelter shelter = ShelterFixture.shelter("imageUrl1");
        Volunteer volunteer = VolunteerFixture.volunteer();
        ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
        FindChatRoomDetailResponse response = FindChatRoomDetailResponse.fromVolunteer(chatRoom);

        given(chatRoomService.findChatRoomDetailByVolunteer(anyLong())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/api/volunteers/chat/rooms/{chatRoomId}", chatRoomId)
                .header(AUTHORIZATION, volunteerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("chatRoomId").description("채팅방 ID")
                ),
                responseFields(
                    fieldWithPath("chatPartnerImageUrl").type(JsonFieldType.STRING)
                        .description("채팅 상대방 이미지 URL"),
                    fieldWithPath("chatPartnerName").type(JsonFieldType.STRING)
                        .description("채팅 상대방 이름")
                )
            ));
    }

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

        when(chatRoomService.registerChatRoom(volunteer.getVolunteerId(), shelter.getShelterId()))
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

    @Test
    @DisplayName("성공: 채팅방 ID 조회 api 호출")
    void findChatRoomId() throws Exception {
        // given
        Volunteer volunteer = VolunteerFixture.volunteer();
        ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", 1L);
        FindChatRoomIdResponse response = new FindChatRoomIdResponse(null);

        when(chatRoomService.findChatRoomId(volunteer.getVolunteerId(), shelter.getShelterId()))
            .thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/volunteers/chat/rooms/shelters/{shelterId}", shelter.getShelterId())
                .header(AUTHORIZATION, volunteerAccessToken));

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("shelterId").description("보호소 ID")
                ),
                responseFields(
                    fieldWithPath("chatRoomId").type(NUMBER).description("채팅방 ID").optional()
                )
            ));
    }
}
