package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.auth.authorization.ShelterOnly;
import com.clova.anifriends.domain.auth.authorization.UserOnly;
import com.clova.anifriends.domain.auth.authorization.VolunteerOnly;
import com.clova.anifriends.domain.chat.dto.request.RegisterChatRoomRequest;
import com.clova.anifriends.domain.chat.dto.response.FindChatMessagesResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomIdResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindUnreadCountResponse;
import com.clova.anifriends.domain.chat.dto.response.RegisterChatRoomResponse;
import com.clova.anifriends.domain.chat.service.ChatRoomService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @VolunteerOnly
    @GetMapping("/volunteers/chat/rooms")
    public ResponseEntity<FindChatRoomsResponse> findChatRoomsByVolunteer(
        @LoginUser Long volunteerId) {
        FindChatRoomsResponse findChatRoomsResponse
            = chatRoomService.findChatRoomsByVolunteer(volunteerId);
        return ResponseEntity.ok(findChatRoomsResponse);
    }

    @ShelterOnly
    @GetMapping("/shelters/chat/rooms")
    public ResponseEntity<FindChatRoomsResponse> findChatRoomsByShelter(
        @LoginUser Long shelterId) {
        FindChatRoomsResponse findChatRoomsResponse
            = chatRoomService.findChatRoomsByShelter(shelterId);
        return ResponseEntity.ok(findChatRoomsResponse);
    }

    @VolunteerOnly
    @GetMapping("/volunteers/chat/rooms/{chatRoomId}")
    public ResponseEntity<FindChatRoomDetailResponse> findChatRoomDetailByVolunteer(
        @PathVariable Long chatRoomId) {
        FindChatRoomDetailResponse findChatRoomDetailResponse
            = chatRoomService.findChatRoomDetailByVolunteer(chatRoomId);
        return ResponseEntity.ok(findChatRoomDetailResponse);
    }

    @ShelterOnly
    @GetMapping("/shelters/chat/rooms/{chatRoomId}")
    public ResponseEntity<FindChatRoomDetailResponse> findChatRoomDetailByShelter(
        @PathVariable Long chatRoomId) {
        FindChatRoomDetailResponse findChatRoomDetailResponse
            = chatRoomService.findChatRoomDetailByShelter(chatRoomId);
        return ResponseEntity.ok(findChatRoomDetailResponse);
    }

    @VolunteerOnly
    @PostMapping("/volunteers/chat/rooms")
    public ResponseEntity<RegisterChatRoomResponse> registerChatRoom(
        @LoginUser Long volunteerId,
        @RequestBody RegisterChatRoomRequest registerChatRoomRequest
    ) {
        RegisterChatRoomResponse registerChatRoomResponse = chatRoomService.registerChatRoom(
            volunteerId,
            registerChatRoomRequest.shelterId());
        URI location
            = URI.create("/api/volunteers/chat/rooms/" + registerChatRoomResponse.chatRoomId());
        return ResponseEntity.created(location).body(registerChatRoomResponse);
    }

    @VolunteerOnly
    @GetMapping("/volunteers/chat/rooms/unread")
    public ResponseEntity<FindUnreadCountResponse> findUnreadCountByVolunteer(
        @LoginUser Long volunteerId) {
        FindUnreadCountResponse findUnreadCountResponse
            = chatRoomService.findUnreadCountByVolunteer(volunteerId);
        return ResponseEntity.ok(findUnreadCountResponse);
    }

    @ShelterOnly
    @GetMapping("/shelters/chat/rooms/unread")
    public ResponseEntity<FindUnreadCountResponse> findUnreadCountByShelter(
        @LoginUser Long shelterId) {
        FindUnreadCountResponse findUnreadCountResponse
            = chatRoomService.findUnreadCountByShelter(shelterId);
        return ResponseEntity.ok(findUnreadCountResponse);
    }

    @UserOnly
    @GetMapping("/chat/rooms/{chatRoomId}/messages")
    public ResponseEntity<FindChatMessagesResponse> findChatMessages(
        @PathVariable Long chatRoomId,
        Pageable pageable) {
        FindChatMessagesResponse findChatMessagesResponse
            = chatRoomService.findChatMessages(chatRoomId, pageable);
        return ResponseEntity.ok(findChatMessagesResponse);
    }

    @VolunteerOnly
    @GetMapping("/volunteers/chat/rooms/shelters/{shelterId}")
    public ResponseEntity<FindChatRoomIdResponse> findChatRoomId(
        @LoginUser Long volunteerId,
        @PathVariable Long shelterId
    ) {
        return ResponseEntity.ok(chatRoomService.findChatRoomId(volunteerId, shelterId));
    }
}
