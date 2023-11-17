package com.clova.anifriends.domain.chat.controller;

import static com.clova.anifriends.domain.auth.jwt.UserRole.ROLE_VOLUNTEER;
import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.message.pub.NewChatMessagePub;
import com.clova.anifriends.domain.chat.message.sub.ChatMessageSub;
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

    private BlockingQueue<NewChatMessagePub> messages;

    private String url;

    @BeforeEach
    void setUp() {
        url = "ws://localhost:" + port + "/ws-stomp";
        messages = new LinkedBlockingDeque<>();

        Volunteer volunteer = VolunteerFixture.volunteer();
        Shelter shelter = ShelterFixture.shelter();
        ChatRoom chatRoom = ChatRoomFixture.chatRoom(volunteer, shelter);

        volunteerRepository.save(volunteer);
        shelterRepository.save(shelter);
        chatRoomRepository.save(chatRoom);
    }

    @Test
    void test() throws Exception {
        // given
        StompSession stompSession = getStompSession();
        Volunteer volunteer = volunteerRepository.findAll().get(0);
        Shelter shelter = shelterRepository.findAll().get(0);
        ChatRoom chatRoom = chatRoomRepository.findAll().get(0);

        ChatMessage chatMessage = ChatMessageFixture.chatMessage(chatRoom,
            volunteer.getVolunteerId(), ROLE_VOLUNTEER);

        NewChatMessagePub.from(chatMessage);

        stompSession.subscribe("/sub/new/chat/rooms/shelters/" + shelter.getShelterId(),
            new StompFrameHandlerImpl<>(NewChatMessagePub.from(chatMessage), messages));
        System.out.println("/sub/new/chat/rooms/shelters/" + shelter.getShelterId());
        // when
        stompSession.send("/pub/new/chat/rooms/" + chatRoom.getChatRoomId() + "/shelters/"
                + shelter.getShelterId(),
            new ChatMessageSub(volunteer.getVolunteerId(), ROLE_VOLUNTEER,
                chatMessage.getMessage()));
        NewChatMessagePub message = messages.poll(5, TimeUnit.SECONDS);

        // then
        assertThat(message).usingRecursiveComparison()
            .ignoringFields("chatMessage.createdAt")
            .isEqualTo(NewChatMessagePub.from(chatMessage));
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
