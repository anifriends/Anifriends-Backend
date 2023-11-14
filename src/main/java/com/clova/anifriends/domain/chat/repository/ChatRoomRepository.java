package com.clova.anifriends.domain.chat.repository;

import com.clova.anifriends.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
