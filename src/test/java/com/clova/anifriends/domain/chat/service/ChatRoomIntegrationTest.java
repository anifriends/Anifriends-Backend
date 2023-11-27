package com.clova.anifriends.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.base.BaseIntegrationTest;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.exception.ChatRoomConflictException;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRoomIntegrationTest extends BaseIntegrationTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Nested
    @DisplayName("findChatRoomDetailByVolunteer 메서드 호출 시")
    class FindChatRoomDetailByVolunteerTest {

        Shelter shelter;
        Volunteer volunteer;
        ChatRoom chatRoom;

        @BeforeEach
        void setUp() {
            shelter = ShelterFixture.shelter("imageUrl");
            volunteer = VolunteerFixture.volunteer("imageUrl");
            chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            chatRoomRepository.save(chatRoom);
        }

        @Test
        @DisplayName("성공: 보호소가 보낸 메시지를 읽음 처리한다.")
        void findChatRoomDetailByVolunteerThenReadShelterMessages() {
            //given
            ChatMessage shelterMessageA = new ChatMessage(chatRoom, shelter.getShelterId(),
                UserRole.ROLE_SHELTER, "안녕하세요");
            ChatMessage shelterMessageB = new ChatMessage(chatRoom, shelter.getShelterId(),
                UserRole.ROLE_SHELTER, "두번째 메시지");
            chatMessageRepository.save(shelterMessageA);
            chatMessageRepository.save(shelterMessageB);
            ChatMessage volunteerMessageA = new ChatMessage(chatRoom, volunteer.getVolunteerId(),
                UserRole.ROLE_VOLUNTEER, "안녕하세요.");
            ChatMessage volunteerMessageB = new ChatMessage(chatRoom, volunteer.getVolunteerId(),
                UserRole.ROLE_VOLUNTEER, "두번째 메시지");
            chatMessageRepository.save(volunteerMessageA);
            chatMessageRepository.save(volunteerMessageB);

            //when
            chatRoomService.findChatRoomDetailByVolunteer(chatRoom.getChatRoomId());

            //then
            List<ChatMessage> messages = chatMessageRepository.findAll();
            List<ChatMessage> shelterMessages = messages.stream()
                .filter(message -> message.getSenderRole() == UserRole.ROLE_SHELTER)
                .toList();
            List<ChatMessage> volunteerMessages = messages.stream()
                .filter(message -> message.getSenderRole() == UserRole.ROLE_VOLUNTEER)
                .toList();
            assertThat(shelterMessages)
                .hasSize(2)
                .allSatisfy(message -> assertThat(message.isRead()).isTrue());
            assertThat(volunteerMessages)
                .hasSize(2)
                .allSatisfy(message -> assertThat(message.isRead()).isFalse());
        }
    }

    @Nested
    @DisplayName("registerChatRoom 메소드 실행 시")
    class RegisterChatRoom {

        @Test
        @DisplayName("예외(ChatRoomConflictException): 중복 채팅방")
        void exceptionWhenDuplicateChatRoom() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            chatRoomRepository.save(chatRoom);

            // when
            Exception exception = catchException(
                () -> chatRoomService.registerChatRoom(volunteer.getVolunteerId(),
                    shelter.getShelterId()));

            // then
            assertThat(exception).isInstanceOf(ChatRoomConflictException.class);
        }


    }
}
