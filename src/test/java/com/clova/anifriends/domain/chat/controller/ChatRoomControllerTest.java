package com.clova.anifriends.domain.chat.controller;

import static org.mockito.ArgumentMatchers.any;
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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.request.RegisterChatRoomRequest;
import com.clova.anifriends.domain.chat.dto.response.FindChatMessagesResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatMessagesResponse.FindChatMessageResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomIdResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse.FindChatRoomResponse;
import com.clova.anifriends.domain.chat.dto.response.FindUnreadCountResponse;
import com.clova.anifriends.domain.chat.dto.response.RegisterChatRoomResponse;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.common.PageInfo;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
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
        RegisterChatRoomRequest request = new RegisterChatRoomRequest(1L);
        long chatRoomId = 1;
        RegisterChatRoomResponse registerChatRoomResponse = new RegisterChatRoomResponse(
            chatRoomId);

        given(chatRoomService.registerChatRoom(anyLong(), anyLong()))
            .willReturn(registerChatRoomResponse);

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
                ),
                responseFields(
                    fieldWithPath("chatRoomId").description("생성된 채팅방 ID")
                )
            ));
    }

    @Test
    @DisplayName("성공: 채팅방 상세 조회(보호소) api 호출 시")
    void findChatRoomDetailByShelter() throws Exception {
        //given
        Long chatRoomId = 1L;
        FindChatRoomDetailResponse findChatRoomDetailResponse
            = new FindChatRoomDetailResponse("imageUrl", "채팅 상대방 이름");

        given(chatRoomService.findChatRoomDetailByShelter(anyLong()))
            .willReturn(findChatRoomDetailResponse);

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/api/shelters/chat/rooms/{chatRoomId}", chatRoomId)
                .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
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
    @DisplayName("안 읽은 총 메시지 수 조회(봉사자) api 호출 시")
    void findUnreadCountByVolunteer() throws Exception {
        //given
        FindUnreadCountResponse findUnreadCountResponse = new FindUnreadCountResponse(5);

        given(chatRoomService.findUnreadCountByVolunteer(anyLong()))
            .willReturn(findUnreadCountResponse);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/volunteers/chat/rooms/unread")
            .header(AUTHORIZATION, volunteerAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("totalUnreadCount").type(JsonFieldType.NUMBER)
                        .description("안 읽은 메시지 수")
                )
            ));
    }

    @Test
    @DisplayName("성공: 채팅방 목록 조회(보호소) api 호출 시")
    void findChatRoomsByShelter() throws Exception {
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

        given(chatRoomService.findChatRoomsByShelter(anyLong())).willReturn(findChatRoomsResponse);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/shelters/chat/rooms")
            .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
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

    @Test
    @DisplayName("성공: 채팅방 메시지 목록 조회 api 호출 시")
    void findChatMessagesResponse() throws Exception {
        //given
        Long chatRoomId = 1L;
        FindChatMessageResponse findChatMessageResponse = new FindChatMessageResponse(1L,
            UserRole.ROLE_VOLUNTEER, "message", LocalDateTime.now());
        PageInfo pageInfo = new PageInfo(1L, false);
        FindChatMessagesResponse findChatMessagesResponse = new FindChatMessagesResponse(
            List.of(findChatMessageResponse), pageInfo);

        given(chatRoomService.findChatMessages(anyLong(), any(Pageable.class))).willReturn(
            findChatMessagesResponse);

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/api/chat/rooms/{chatRoomId}/messages", chatRoomId)
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                pathParameters(
                    parameterWithName("chatRoomId").description("채팅방 ID")
                ),
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("사용자 액세스 토큰")
                ),
                queryParameters(
                    parameterWithName("pageNumber").description("페이지 번호"),
                    parameterWithName("pageSize").description("페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("chatMessages").type(JsonFieldType.ARRAY)
                        .description("채팅방 메시지 목록"),
                    fieldWithPath("chatMessages[].chatSenderId").type(JsonFieldType.NUMBER)
                        .description("채팅 메시지 발송자 ID"),
                    fieldWithPath("chatMessages[].chatSenderRole").type(JsonFieldType.STRING)
                        .description("채팅 메시지 발송자 역할"),
                    fieldWithPath("chatMessages[].chatMessage").type(JsonFieldType.STRING)
                        .description("채팅 메시지 내용"),
                    fieldWithPath("chatMessages[].createdAt").type(JsonFieldType.STRING)
                        .description("채팅 메시지 발송 시점"),
                    fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                    fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER)
                        .description("총 요소 개수"),
                    fieldWithPath("pageInfo.hasNext").type(JsonFieldType.BOOLEAN)
                        .description("다음 페이지 존재 여부")
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

    @Test
    @DisplayName("성공: 안 읽은 메시지 총 수 조회(보호소) api 호출 시")
    void findUnreadCountByShelter() throws Exception {
        //given
        FindUnreadCountResponse findUnreadCountResponse = new FindUnreadCountResponse(5);

        given(chatRoomService.findUnreadCountByShelter(anyLong()))
            .willReturn(findUnreadCountResponse);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/shelters/chat/rooms/unread")
            .header(AUTHORIZATION, shelterAccessToken));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("보호소 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("totalUnreadCount").type(NUMBER).description("안 읽은 메시지 총 개수")
                )
            ));
    }
}
