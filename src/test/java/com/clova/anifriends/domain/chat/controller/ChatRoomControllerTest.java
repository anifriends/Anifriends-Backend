package com.clova.anifriends.domain.chat.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse.FindChatRoomResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
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

        given(chatService.findChatRoomsByVolunteer(1L)).willReturn(findChatRoomsResponse);

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
                    fieldWithPath("chatRooms[].chatRoomId").type(JsonFieldType.NUMBER)
                        .description("채팅방 ID"),
                    fieldWithPath("chatRooms[].chatRecentMessage").type(JsonFieldType.STRING)
                        .description("채팅방 최근 메시지"),
                    fieldWithPath("chatRooms[].chatPartnerName").type(JsonFieldType.STRING)
                        .description("채팅방 상대방 이름"),
                    fieldWithPath("chatRooms[].charPartnerImageUrl").type(JsonFieldType.STRING)
                        .description("채팅방 상대방 이미지 URL"),
                    fieldWithPath("chatRooms[].createdAt").type(JsonFieldType.STRING)
                        .description("채팅방 최근 메시지 발송 시간"),
                    fieldWithPath("chatRooms[].chatUnReadCount").type(JsonFieldType.NUMBER)
                        .description("채팅방 최근 메시지 발송 시간")
                )
            ));
    }
}