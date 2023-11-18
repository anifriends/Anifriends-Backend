package com.clova.anifriends.domain.chat.repository;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ChatMessage cm set cm.isRead = true"
        + " where cm.chatRoom = :chatRoom"
        + " and cm.senderRole != :senderRole")
    void readPartnerMessages(
        @Param("chatRoom") ChatRoom chatRoom,
        @Param("senderRole") UserRole senderRole);

    Page<ChatMessage> findByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom, Pageable pageable);
}
