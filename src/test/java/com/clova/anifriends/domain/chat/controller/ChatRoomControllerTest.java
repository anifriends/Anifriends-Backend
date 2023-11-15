package com.clova.anifriends.domain.chat.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChatRoomControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 채팅방 상세 조회(봉사자) api 호출 시")
    void findChatRoomDetailByVolunteer() throws Exception {
        //given
        Long chatRoomId = 1L;
        Shelter shelter = ShelterFixture.shelter("imageUrl1");
        Volunteer volunteer = VolunteerFixture.volunteer();
        ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
        FindChatRoomDetailResponse response = FindChatRoomDetailResponse.fromVolunteer(chatRoom);

        given(chatService.findChatRoomDetailByVolunteer(anyLong())).willReturn(response);

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
}