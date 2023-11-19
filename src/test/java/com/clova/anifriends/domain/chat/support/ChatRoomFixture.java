package com.clova.anifriends.domain.chat.support;

import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import org.springframework.test.util.ReflectionTestUtils;

public class ChatRoomFixture {

    public static ChatRoom chatRoom(Volunteer volunteer, Shelter shelter) {
        ChatRoom chatRoom = new ChatRoom(volunteer, shelter);
        ReflectionTestUtils.setField(chatRoom, "chatRoomId", 1L);
        return chatRoom;
    }
}
