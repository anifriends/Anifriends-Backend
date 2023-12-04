package com.clova.anifriends.domain.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.support.ChatMessageFixture;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChatMessageRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("readPartnerMessages 메서드 실행 시")
    class ReadPartnerMessagesTest {

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
        @DisplayName("성공")
        void readPartnerMessagesWhenVolunteerRead() {
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
            chatMessageRepository.readPartnerMessages(chatRoom, UserRole.ROLE_VOLUNTEER);

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
    @DisplayName("findUnreadCount 메서드 호출 시")
    class FindUnreadCountTest {

        Volunteer volunteer;
        Shelter shelter;
        ChatRoom chatRoom;

        @BeforeEach
        void setUp() {
            volunteer = VolunteerFixture.volunteer();
            shelter = ShelterFixture.shelter();
            chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            volunteerRepository.save(volunteer);
            shelterRepository.save(shelter);
            chatRoomRepository.save(chatRoom);
        }

        @Test
        @DisplayName("성공")
        void findUnreadCount() {
            //given
            int chatMessagesEachCount = 10;
            List<ChatMessage> oldChatMessagesByShelter = IntStream.range(0, chatMessagesEachCount)
                .mapToObj(i -> ChatMessageFixture.shelterMessage(chatRoom, shelter))
                .toList();
            chatMessageRepository.saveAll(oldChatMessagesByShelter);
            chatMessageRepository.readPartnerMessages(chatRoom, UserRole.ROLE_VOLUNTEER);
            List<ChatMessage> chatMessagesByShelter = IntStream.range(0, chatMessagesEachCount)
                .mapToObj(i -> ChatMessageFixture.shelterMessage(chatRoom, shelter))
                .toList();
            List<ChatMessage> chatMessagesByVolunteer = IntStream.range(0, chatMessagesEachCount)
                .mapToObj(i -> ChatMessageFixture.volunteerMessage(chatRoom, volunteer))
                .toList();
            chatMessageRepository.saveAll(chatMessagesByShelter);
            chatMessageRepository.saveAll(chatMessagesByVolunteer);

            //when
            Long unreadCount = chatMessageRepository.findUnreadCountByVolunteer(volunteer);

            //then
            assertThat(unreadCount).isEqualTo(chatMessagesEachCount);
        }
    }
}
