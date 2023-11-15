package com.clova.anifriends.domain.chat.repository;

import com.clova.anifriends.domain.chat.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr"
        + " join fetch cr.shelter"
        + " where cr.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findByIdWithShelter(Long chatRoomId);
}
