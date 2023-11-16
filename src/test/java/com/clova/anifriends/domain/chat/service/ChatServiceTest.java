package com.clova.anifriends.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse.FindChatRoomResponse;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import com.clova.anifriends.domain.chat.support.ChatRoomDtoFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @InjectMocks
    ChatService chatService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    VolunteerRepository volunteerRepository;

    @Nested
    @DisplayName("findChatRoomByVolunteer 메서드 실행 시")
    class FindChatRoomByVolunteerTest {

        @Test
        @DisplayName("성공")
        void findChatRoomsByVolunteer() {
            //given
            long chatRoomId = 1L;
            String chatRecentMessage = "최근 메시지";
            String chatPartnerName = "상대방 이름";
            String chatPartnerImageUrl = "imageUrl";
            LocalDateTime createdAt = LocalDateTime.now();
            long chatUnReadCount = 5L;
            FindChatRoomResult chatRoomResult = ChatRoomDtoFixture.findChatRoomResult(
                chatRoomId, chatRecentMessage, chatPartnerName, chatPartnerImageUrl, createdAt,
                chatUnReadCount);
            List<FindChatRoomResult> findChatRoomsResult = List.of(chatRoomResult);
            Volunteer volunteer = VolunteerFixture.volunteer();

            given(volunteerRepository.findById(anyLong())).willReturn(Optional.of(volunteer));
            given(chatRoomRepository.findChatRoomsByVolunteer(any(Volunteer.class))).willReturn(
                findChatRoomsResult);

            //when
            FindChatRoomsResponse findChatRoomsResponse = chatService.findChatRoomsByVolunteer(1L);

            //then
            List<FindChatRoomResponse> findChatRooms = findChatRoomsResponse.chatRooms();
            FindChatRoomResponse chatRoomResponse = findChatRooms.get(0);
            assertThat(chatRoomResponse.chatRoomId()).isEqualTo(chatRoomId);
            assertThat(chatRoomResponse.chatRecentMessage()).isEqualTo(chatRecentMessage);
            assertThat(chatRoomResponse.chatPartnerName()).isEqualTo(chatPartnerName);
            assertThat(chatRoomResponse.charPartnerImageUrl()).isEqualTo(chatPartnerImageUrl);
            assertThat(chatRoomResponse.createdAt()).isEqualTo(createdAt);
            assertThat(chatRoomResponse.chatUnReadCount()).isEqualTo(chatUnReadCount);
        }
    }
}
