package com.clova.anifriends.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.NewChatMessageResponse;
import com.clova.anifriends.domain.chat.exception.ChatRoomNotFoundException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.support.ChatMessageFixture;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Nested
    @DisplayName("registerChatMessage 메소드는")
    class RegisterMessageTest {

        @Test
        @DisplayName("성공")
        void registerChatMessage() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            UserRole senderRole = UserRole.ROLE_VOLUNTEER;
            Long senderId = 1L;

            ChatMessage chatMessage = ChatMessageFixture.chatMessage(chatRoom, senderId,
                senderRole);
            NewChatMessageResponse expect = NewChatMessageResponse.from(chatMessage);

            when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));

            // when
            NewChatMessageResponse result = chatMessageService.registerNewChatMessage(1L,
                chatMessage.getSenderId(), chatMessage.getSenderRole(), chatMessage.getMessage());

            // then
            verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
            assertThat(result).usingRecursiveComparison().isEqualTo(expect);

        }

        @Test
        @DisplayName("예외(ChatRoomNotFoundException): 채팅방이 존재하지 않을 경우")
        void exceptionWhenChatRoomIsNotExist() {
            // given
            UserRole senderRole = UserRole.ROLE_VOLUNTEER;
            String message = "악";

            when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> chatMessageService.registerNewChatMessage(1L, 1L, senderRole, message));

            // then
            assertThat(exception).isInstanceOf(ChatRoomNotFoundException.class);
        }
    }


}
