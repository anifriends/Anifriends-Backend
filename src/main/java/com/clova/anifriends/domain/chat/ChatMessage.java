package com.clova.anifriends.domain.chat;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {

    @Id
    @Column(name = "chat_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_role")
    private UserRole senderRole;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    public ChatMessage(ChatRoom chatRoom, Long senderId, UserRole senderRole, String message) {
        this.chatRoom = chatRoom;
        this.senderId = senderId;
        this.senderRole = senderRole;
        this.message = message;
        this.isRead = false;
    }
}
