package com.clova.anifriends.domain.chat;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.exception.ChatMessageBadRequestException;
import com.clova.anifriends.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {

    public static final int MAX_MESSAGE_LENGTH = 1000;
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
        validateChatRoom(chatRoom);
        validateSenderId(senderId);
        validateSenderRole(senderRole);
        validateMessage(message);

        this.chatRoom = chatRoom;
        this.senderId = senderId;
        this.senderRole = senderRole;
        this.message = message;
        this.isRead = false;
    }

    private void validateMessage(String message) {
        if (Objects.isNull(message) || message.isBlank()) {
            throw new ChatMessageBadRequestException("메시지가 존재하지 않습니다.");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new ChatMessageBadRequestException(
                MessageFormat.format("메시지는 {0}자 이하로 입력해주세요.", MAX_MESSAGE_LENGTH)
            );
        }
    }

    private void validateSenderRole(UserRole senderRole) {
        if (Objects.isNull(senderRole)) {
            throw new ChatMessageBadRequestException("메시지 전송자의 타입이 존재하지 않습니다.");
        }
    }

    private void validateSenderId(Long senderId) {
        if (Objects.isNull(senderId)) {
            throw new ChatMessageBadRequestException("메시지 전송자의 아이디가 존재하지 않습니다.");
        }
    }

    private void validateChatRoom(ChatRoom chatRoom) {
        if (Objects.isNull(chatRoom)) {
            throw new ChatMessageBadRequestException("채팅방이 존재하지 않습니다.");
        }
    }
}
