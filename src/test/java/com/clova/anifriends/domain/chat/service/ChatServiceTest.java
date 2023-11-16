package com.clova.anifriends.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.exception.ChatNotFoundException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
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
class ChatServiceTest {

    @InjectMocks
    ChatService chatService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Nested
    @DisplayName("findChatRoomDetail 메서드 호출 시")
    class FindChatRoomDetailsTest {

        @Test
        @DisplayName("성공")
        void findChatRoomDetail() {
            //given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);

            given(chatRoomRepository.findByIdWithShelter(anyLong()))
                .willReturn(Optional.of(chatRoom));

            //when
            FindChatRoomDetailResponse chatRoomDetail = chatService.findChatRoomDetailByVolunteer(
                1L);

            //then
            assertThat(chatRoomDetail.chatPartnerName()).isEqualTo(shelter.getName());
            assertThat(chatRoomDetail.chatPartnerImageUrl()).isEqualTo(shelter.getImage());
        }

        @Test
        @DisplayName("예외(ChatNotFoundException): 존재하지 않는 채팅방")
        void exceptionWhenChatRoomNotFound() {
            //given
            given(chatRoomRepository.findByIdWithShelter(anyLong())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> chatService.findChatRoomDetailByVolunteer(
                1L));

            //then
            assertThat(exception).isInstanceOf(ChatNotFoundException.class);
        }
    }
}