package com.clova.anifriends.domain.chat.support;

import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;

public class ChatRoomFixture {

    public static ChatRoom chatRoom(Volunteer volunteer, Shelter shelter) {
        return new ChatRoom(volunteer, shelter);
    }
}
