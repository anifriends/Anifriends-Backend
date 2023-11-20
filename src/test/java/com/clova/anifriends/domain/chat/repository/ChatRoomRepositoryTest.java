package com.clova.anifriends.domain.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.Assertions.within;

import com.clova.anifriends.base.BaseRepositoryTest;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

class ChatRoomRepositoryTest extends BaseRepositoryTest {

    @Nested
    @DisplayName("findChatRoomsByVolunteer 메서드 실행 시")
    class FindChatRoomsByVolunteerTest {

        Volunteer volunteer;
        List<Shelter> shelters;
        List<ChatRoom> chatRooms;

        @BeforeEach
        void setUp() {
            volunteer = VolunteerFixture.volunteer();
            shelters = ShelterFixture.shelters(5);
            chatRooms = shelters.stream()
                .map(shelter -> new ChatRoom(volunteer, shelter))
                .toList();
        }

        @Test
        @DisplayName("성공")
        void findChatRoomsByVolunteer() {
            //given
            LocalDateTime now = LocalDateTime.now();
            shelterRepository.saveAll(shelters);
            volunteerRepository.save(volunteer);
            chatRoomRepository.saveAll(chatRooms);
            String oldMessage = "첫 번째";
            String secondOldMessage = "두 번째";
            String recentMessage = "세 번째";
            List<ChatMessage> oldMessages = IntStream.range(0, shelters.size())
                .mapToObj(i -> new ChatMessage(chatRooms.get(i), shelters.get(i).getShelterId(),
                    UserRole.ROLE_SHELTER, oldMessage))
                .toList();
            List<ChatMessage> secondOldMessages = IntStream.range(0, shelters.size())
                .mapToObj(i -> new ChatMessage(chatRooms.get(i), shelters.get(i).getShelterId(),
                    UserRole.ROLE_SHELTER, secondOldMessage))
                .toList();
            List<ChatMessage> recentMessages = IntStream.range(0, shelters.size())
                .mapToObj(i -> new ChatMessage(chatRooms.get(i), shelters.get(i).getShelterId(),
                    UserRole.ROLE_SHELTER, recentMessage))
                .toList();
            chatMessageRepository.saveAll(oldMessages);
            chatMessageRepository.saveAll(secondOldMessages);
            chatMessageRepository.saveAll(recentMessages);
            oldMessages.forEach(message -> ReflectionTestUtils.setField(message, "createdAt",
                now.minusDays(2)));
            secondOldMessages.forEach(message -> ReflectionTestUtils.setField(message, "createdAt",
                now.minusDays(1)));
            entityManager.flush();

            //when
            List<FindChatRoomResult> chatRooms
                = chatRoomRepository.findChatRoomsByVolunteer(volunteer);

            //then
            assertThat(chatRooms)
                .hasSize(5)
                .allSatisfy(chatRoom -> {
                    assertThat(chatRoom.getCreatedAt())
                        .isCloseTo(now, within(5, ChronoUnit.SECONDS));
                    assertThat(chatRoom.getChatRecentMessage()).isEqualTo(recentMessage);
                });
        }
    }

    @Nested
    @DisplayName("findChatRoomsByShelter 메서드 실행 시")
    class FindChatRoomsByShelterTest {

        List<Volunteer> volunteers;
        Shelter shelter;
        List<ChatRoom> chatRooms;

        @BeforeEach
        void setUp() {
            volunteers = VolunteerFixture.volunteers(5);
            shelter = ShelterFixture.shelter("imageUrl");
            chatRooms = volunteers.stream()
                .map(volunteer -> new ChatRoom(volunteer, shelter))
                .toList();
        }

        @Test
        @DisplayName("성공")
        void findChatRoomsByVolunteer() {
            //given
            LocalDateTime now = LocalDateTime.now();
            shelterRepository.save(shelter);
            volunteerRepository.saveAll(volunteers);
            chatRoomRepository.saveAll(chatRooms);
            String oldMessage = "첫 번째";
            String secondOldMessage = "두 번째";
            String recentMessage = "세 번째";
            List<ChatMessage> oldMessages = IntStream.range(0, volunteers.size())
                .mapToObj(i -> new ChatMessage(chatRooms.get(i), volunteers.get(i).getVolunteerId(),
                    UserRole.ROLE_SHELTER, oldMessage))
                .toList();
            List<ChatMessage> secondOldMessages = IntStream.range(0, volunteers.size())
                .mapToObj(i -> new ChatMessage(chatRooms.get(i), volunteers.get(i).getVolunteerId(),
                    UserRole.ROLE_SHELTER, secondOldMessage))
                .toList();
            List<ChatMessage> recentMessages = IntStream.range(0, volunteers.size())
                .mapToObj(i -> new ChatMessage(chatRooms.get(i), volunteers.get(i).getVolunteerId(),
                    UserRole.ROLE_SHELTER, recentMessage))
                .toList();
            chatMessageRepository.saveAll(oldMessages);
            chatMessageRepository.saveAll(secondOldMessages);
            chatMessageRepository.saveAll(recentMessages);
            oldMessages.forEach(message -> ReflectionTestUtils.setField(message, "createdAt",
                now.minusDays(2)));
            secondOldMessages.forEach(message -> ReflectionTestUtils.setField(message, "createdAt",
                now.minusDays(1)));
            entityManager.flush();

            //when
            List<FindChatRoomResult> chatRooms
                = chatRoomRepository.findChatRoomsByShelter(shelter);

            //then
            assertThat(chatRooms)
                .hasSize(5)
                .allSatisfy(chatRoom -> {
                    assertThat(chatRoom.getCreatedAt())
                        .isCloseTo(now, within(5, ChronoUnit.SECONDS));
                    assertThat(chatRoom.getChatRecentMessage()).isEqualTo(recentMessage);
                });
        }
    }

    @Nested
    @DisplayName("save 메서드 실행 시")
    class SaveTest {

        @Test
        @DisplayName("예외(DataIntegrityViolationException): 중복된 채팅방")
        void exceptionWhenDuplicateChatRoom() {
            // given
            Shelter shelter = ShelterFixture.shelter();
            Volunteer volunteer = VolunteerFixture.volunteer();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            ChatRoom duplicateChatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);

            shelterRepository.save(shelter);
            volunteerRepository.save(volunteer);
            chatRoomRepository.save(chatRoom);

            // when
            Exception exception = catchException(() -> chatRoomRepository.save(duplicateChatRoom));

            // then
            assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);

        }
    }
}
