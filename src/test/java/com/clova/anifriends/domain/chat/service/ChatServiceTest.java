package com.clova.anifriends.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
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
    private ChatService chatService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private VolunteerRepository volunteerRepository;

    @Nested
    @DisplayName("registerChatRoom 메소드 실행 시")
    class RegisterChatRoom {

        @Test
        @DisplayName("성공")
        void registerChatRoom() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));
            when(shelterRepository.findById(anyLong())).thenReturn(Optional.of(shelter));

            // when
            chatService.registerChatRoom(1L, 1L);

            // then
            verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
        }

        @Test
        @DisplayName("예외(VolunteerNotFoundException): 봉사자가 존재하지 않을 때")
        void exceptionWhenVolunteerIsNull() {
            // given
            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> chatService.registerChatRoom(1L, 1L));

            // then
            assertThat(exception).isInstanceOf(VolunteerNotFoundException.class);
        }

        @Test
        @DisplayName("예외(ShelterNotFoundException): 보호소가 존재하지 않을 때")
        void exceptionWhenShelterIsNull() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();

            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.of(volunteer));
            when(shelterRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> chatService.registerChatRoom(1L, 1L));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }

}
