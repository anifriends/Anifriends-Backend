package com.clova.anifriends.domain.chat.repository;

import com.clova.anifriends.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
