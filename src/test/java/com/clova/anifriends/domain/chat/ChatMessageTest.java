package com.clova.anifriends.domain.chat;

import static com.clova.anifriends.domain.auth.jwt.UserRole.ROLE_VOLUNTEER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.exception.ChatMessageBadRequestException;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChatMessageTest {

    @Nested
    @DisplayName("ChatMessage 생성 시")
    class NewChatMessageTest {

        @Test
        @DisplayName("성공: 메시지가 1자인 경우")
        void newChatMessage1() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long senderId = 1L;
            UserRole senderRole = ROLE_VOLUNTEER;
            String message = "악";

            // when
            ChatMessage result = new ChatMessage(chatRoom, senderId, senderRole, message);

            // then
            assertSoftly(softAssertion -> {
                softAssertion.assertThat(result.getChatRoom()).isEqualTo(chatRoom);
                softAssertion.assertThat(result.getSenderId()).isEqualTo(senderId);
                softAssertion.assertThat(result.getSenderRole()).isEqualTo(senderRole);
                softAssertion.assertThat(result.getMessage()).isEqualTo(message);
            });
        }

        @Test
        @DisplayName("성공: 메시지가 1000자인 경우")
        void newChatMessage2() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long senderId = 1L;
            UserRole senderRole = ROLE_VOLUNTEER;
            String message = "악".repeat(1000);

            // when
            ChatMessage result = new ChatMessage(chatRoom, senderId, senderRole, message);

            // then
            assertSoftly(softAssertion -> {
                softAssertion.assertThat(result.getChatRoom()).isEqualTo(chatRoom);
                softAssertion.assertThat(result.getSenderId()).isEqualTo(senderId);
                softAssertion.assertThat(result.getSenderRole()).isEqualTo(senderRole);
                softAssertion.assertThat(result.getMessage()).isEqualTo(message);
            });
        }

        @Test
        @DisplayName("예외(ChatMessageBadRequestException): 채팅방이 존재하지 않을 경우")
        void exceptionWhenChatRoomIsNull() {
            // given
            ChatRoom nullChatRoom = null;
            Long senderId = 1L;
            UserRole senderRole = ROLE_VOLUNTEER;
            String message = "안녕하세요";

            // when
            Exception exception = catchException(
                () -> new ChatMessage(nullChatRoom, senderId, senderRole, message));

            // then
            assertThat(exception).isInstanceOf(ChatMessageBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ChatMessageBadRequestException): 전송자 타입이 존재하지 않을 경우")
        void exceptionWhenSenderTypeIsNull() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long senderId = 1L;
            UserRole nullSenderRole = null;
            String message = "안녕하세요";

            // when
            Exception exception = catchException(
                () -> new ChatMessage(chatRoom, senderId, nullSenderRole, message));

            // then
            assertThat(exception).isInstanceOf(ChatMessageBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ChatMessageBadRequestException): 전송자 ID가 존재하지 않을 경우")
        void exceptionWhenSenderIDIsNull() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long nullSenderId = null;
            UserRole senderRole = ROLE_VOLUNTEER;
            String message = "안녕하세요";

            // when
            Exception exception = catchException(
                () -> new ChatMessage(chatRoom, nullSenderId, senderRole, message));

            // then
            assertThat(exception).isInstanceOf(ChatMessageBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ChatMessageBadRequestException): 메시지가 null인 경우")
        void exceptionWhenMessageIsNull() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long senderId = 1L;
            UserRole senderRole = ROLE_VOLUNTEER;
            String nullMessage = null;

            // when
            Exception exception = catchException(
                () -> new ChatMessage(chatRoom, senderId, senderRole, nullMessage));

            // then
            assertThat(exception).isInstanceOf(ChatMessageBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ChatMessageBadRequestException): 메시지가 비어있는 경우")
        void exceptionWhenMessageIsBlank() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long senderId = 1L;
            UserRole senderRole = ROLE_VOLUNTEER;
            String blankMessage = "";

            // when
            Exception exception = catchException(
                () -> new ChatMessage(chatRoom, senderId, senderRole, blankMessage));

            // then
            assertThat(exception).isInstanceOf(ChatMessageBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ChatMessageBadRequestException): 메시지가 1000자 넘는 경우")
        void exceptionWhenMessageIsOver1000() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();
            ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);
            Long senderId = 1L;
            UserRole senderRole = ROLE_VOLUNTEER;
            String overMessage = "악".repeat(1001);

            // when
            Exception exception = catchException(
                () -> new ChatMessage(chatRoom, senderId, senderRole, overMessage));

            // then
            assertThat(exception).isInstanceOf(ChatMessageBadRequestException.class);
        }

    }

}
