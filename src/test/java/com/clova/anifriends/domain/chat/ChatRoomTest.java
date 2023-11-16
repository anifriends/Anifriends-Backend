package com.clova.anifriends.domain.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.clova.anifriends.domain.chat.exception.ChatRoomBadRequestException;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChatRoomTest {

    @Nested
    @DisplayName("ChatRoom 생성 시")
    class NewChatRoom {

        @Test
        @DisplayName("성공")
        void createChatRoom() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter shelter = ShelterFixture.shelter();

            // when
            Exception exception = catchException(() -> new ChatRoom(volunteer, shelter));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외(ChatRoomBadRequestException): volunteer 가 null 일 때")
        void exceptionWhenVolunteerIsNull() {
            // given
            Volunteer nullVolunteer = null;
            Shelter shelter = ShelterFixture.shelter();

            // when
            Exception exception = catchException(() -> new ChatRoom(nullVolunteer, shelter));

            // then
            assertThat(exception).isInstanceOf(ChatRoomBadRequestException.class);
        }

        @Test
        @DisplayName("예외(ChatRoomBadRequestException): shelter 가 null 일 때")
        void exceptionWhenShelterIsNull() {
            // given
            Volunteer volunteer = VolunteerFixture.volunteer();
            Shelter nullShelter = null;

            // when
            Exception exception = catchException(() -> new ChatRoom(volunteer, nullShelter));

            // then
            assertThat(exception).isInstanceOf(ChatRoomBadRequestException.class);
        }
    }

}
