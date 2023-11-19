package com.clova.anifriends.domain.chat.dto.response;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.common.PageInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindChatMessagesResponse(List<FindChatMessageResponse> chatMessages, PageInfo pageInfo) {

    public record FindChatMessageResponse(
        Long chatSenderId,
        UserRole chatSenderRole,
        String chatMessage,
        LocalDateTime createdAt) {

        public static FindChatMessageResponse from(ChatMessage chatMessage) {
            return new FindChatMessageResponse(
                chatMessage.getSenderId(),
                chatMessage.getSenderRole(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt());
        }
    }

    public static FindChatMessagesResponse from(Page<ChatMessage> chatMessages) {
        List<FindChatMessageResponse> content = chatMessages.getContent()
            .stream()
            .map(FindChatMessageResponse::from)
            .toList();
        PageInfo pageInfo = PageInfo.of(chatMessages.getTotalElements(), chatMessages.hasNext());
        return new FindChatMessagesResponse(content, pageInfo);
    }
}
