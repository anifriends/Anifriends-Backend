package com.clova.anifriends.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.exception.ChatNotFoundException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
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
class ChatRoomServiceTest {

    @InjectMocks
    ChatRoomService chatRoomService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    @Mock
    ChatMessageRepository chatMessageRepository;

    @Mock
    VolunteerRepository volunteerRepository;

    @Mock
    ShelterRepository shelterRepository;

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
            FindChatRoomDetailResponse chatRoomDetail = chatRoomService.findChatRoomDetailByVolunteer(
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
            Exception exception = catchException(() -> chatRoomService.findChatRoomDetailByVolunteer(
                1L));

            //then
            assertThat(exception).isInstanceOf(ChatNotFoundException.class);
        }
    }

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
            chatRoomService.registerChatRoom(1L, 1L);

            // then
            verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
        }

        @Test
        @DisplayName("예외(VolunteerNotFoundException): 봉사자가 존재하지 않을 때")
        void exceptionWhenVolunteerIsNull() {
            // given
            when(volunteerRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> chatRoomService.registerChatRoom(1L, 1L));

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
            Exception exception = catchException(() -> chatRoomService.registerChatRoom(1L, 1L));

            // then
            assertThat(exception).isInstanceOf(ShelterNotFoundException.class);
        }
    }
}
