package com.clova.anifriends.domain.chat.controller;

import static com.clova.anifriends.domain.auth.jwt.UserRole.ROLE_VOLUNTEER;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.request.ChatMessageRequest;
import com.clova.anifriends.domain.chat.dto.response.NewChatMessageResponse;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.support.ChatMessageFixture;
import com.clova.anifriends.domain.chat.support.ChatRoomFixture;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketStompTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private BlockingQueue<NewChatMessageResponse> newChatMessageResponses;

    private BlockingQueue<ChatMessageResponse> chatMessageResponses;

    private StompSession stompSession;

    private String url;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        url = "ws://localhost:" + port + "/ws-stomp";
        stompSession = getStompSession();

        newChatMessageResponses = new LinkedBlockingDeque<>();
        chatMessageResponses = new LinkedBlockingDeque<>();

        Volunteer volunteer = VolunteerFixture.volunteer();
        Shelter shelter = ShelterFixture.shelter();
        ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);

        volunteerRepository.save(volunteer);
        shelterRepository.save(shelter);
        chatRoomRepository.save(chatRoom);
    }

    @Test
    @DisplayName("새로운 채팅방에서 메시지 전송 api 호출 시")
    void newChatMessage() throws InterruptedException {
        // given
        Volunteer volunteer = volunteerRepository.findAll().get(0);
        Shelter shelter = shelterRepository.findAll().get(0);
        ChatRoom chatRoom = chatRoomRepository.findAll().get(0);

        ChatMessage chatMessage = ChatMessageFixture.chatMessage(chatRoom,
            volunteer.getVolunteerId(), ROLE_VOLUNTEER);

        ChatMessageRequest request = new ChatMessageRequest(volunteer.getVolunteerId(),
            ROLE_VOLUNTEER, chatMessage.getMessage());
        NewChatMessageResponse response = NewChatMessageResponse.from(chatMessage);

        stompSession.subscribe("/sub/new/chat/rooms/shelters/" + shelter.getShelterId(),
            new StompFrameHandlerImpl<>(response, newChatMessageResponses));

        // when
        stompSession.send("/pub/new/chat/rooms/" + chatRoom.getChatRoomId() + "/shelters/"
            + shelter.getShelterId(), request);
        NewChatMessageResponse newChatMessageResponse = newChatMessageResponses.poll(5,
            TimeUnit.SECONDS);

        // then
        assertThat(newChatMessageResponse).usingRecursiveComparison()
            .ignoringFields("chatMessage.createdAt")
            .isEqualTo(NewChatMessageResponse.from(chatMessage));
    }

    @Test
    @DisplayName("기존 채팅방에서 메시지 전송 api 호출 시")
    void ChatMessage() throws InterruptedException {
        // given
        Volunteer volunteer = volunteerRepository.findAll().get(0);
        ChatRoom chatRoom = chatRoomRepository.findAll().get(0);

        ChatMessage chatMessage = ChatMessageFixture.chatMessage(chatRoom,
            volunteer.getVolunteerId(), ROLE_VOLUNTEER);

        ChatMessageRequest request = new ChatMessageRequest(volunteer.getVolunteerId(),
            ROLE_VOLUNTEER, chatMessage.getMessage());
        ChatMessageResponse response = ChatMessageResponse.from(chatMessage);

        stompSession.subscribe("/sub/chat/rooms/" + chatRoom.getChatRoomId(),
            new StompFrameHandlerImpl<>(response, chatMessageResponses));

        // when
        stompSession.send("/pub/chat/rooms/" + chatRoom.getChatRoomId(), request);
        ChatMessageResponse chatMessageResponse = chatMessageResponses.poll(5, TimeUnit.SECONDS);

        // then
        assertThat(chatMessageResponse).usingRecursiveComparison()
            .ignoringFields("createdAt")
            .isEqualTo(ChatMessageResponse.from(chatMessage));
    }

    private StompSession getStompSession()
        throws ExecutionException, InterruptedException, TimeoutException {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        SockJsClient sockJsClient = new SockJsClient(List.of(webSocketTransport));
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(sockJsClient);

        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        ObjectMapper objectMapper = messageConverter.getObjectMapper();
        objectMapper.registerModules(new JavaTimeModule(), new ParameterNamesModule());
        webSocketStompClient.setMessageConverter(messageConverter);

        return webSocketStompClient
            .connectAsync(url, new StompSessionHandlerAdapter() {
            })
            .get(2, TimeUnit.SECONDS);
    }

}

class StompFrameHandlerImpl<T> implements StompFrameHandler {

    private final T response;
    private final BlockingQueue<T> responses;

    public StompFrameHandlerImpl(final T response, final BlockingQueue<T> responses) {
        this.response = response;
        this.responses = responses;
    }

    @Override
    public Type getPayloadType(final StompHeaders headers) {
        return response.getClass();
    }

    @Override
    public void handleFrame(final StompHeaders headers, final Object payload) {
        responses.offer((T) payload);
    }
}
