package com.clova.anifriends.domain.chat.repository;

import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ChatMessage cm set cm.isRead = true"
        + " where cm.chatRoom = :chatRoom")
    void readAllMessage(ChatRoom chatRoom);
}
