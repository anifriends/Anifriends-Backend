package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.FindChatMessagesResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomIdResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindUnreadCountResponse;
import com.clova.anifriends.domain.chat.dto.response.RegisterChatRoomResponse;
import com.clova.anifriends.domain.chat.exception.ChatNotFoundException;
import com.clova.anifriends.domain.chat.exception.ChatRoomConflictException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.aspect.DataIntegrityHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final VolunteerRepository volunteerRepository;
    private final ShelterRepository shelterRepository;

    @Transactional
    public FindChatRoomDetailResponse findChatRoomDetailByVolunteer(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoomWithShelter(chatRoomId);
        chatMessageRepository.readPartnerMessages(chatRoom, UserRole.ROLE_VOLUNTEER);
        return FindChatRoomDetailResponse.fromVolunteer(chatRoom);
    }

    @Transactional
    public FindChatRoomDetailResponse findChatRoomDetailByShelter(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoomWithVolunteer(chatRoomId);
        chatMessageRepository.readPartnerMessages(chatRoom, UserRole.ROLE_SHELTER);
        return FindChatRoomDetailResponse.fromShelter(chatRoom);
    }

    @Transactional
    @DataIntegrityHandler(message = "이미 존재하는 채팅방입니다.", exceptionClass = ChatRoomConflictException.class)
    public RegisterChatRoomResponse registerChatRoom(Long volunteerId, Long shelterId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        Shelter shelter = getShelter(shelterId);
        ChatRoom chatRoom = new ChatRoom(volunteer, shelter);
        chatRoomRepository.save(chatRoom);
        return RegisterChatRoomResponse.from(chatRoom);
    }

    @Transactional(readOnly = true)
    public FindChatRoomsResponse findChatRoomsByVolunteer(Long volunteerId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        List<FindChatRoomResult> findChatRoomResult
            = chatRoomRepository.findChatRoomsByVolunteer(volunteer);
        return ChatRoomMapper.resultToResponse(findChatRoomResult);
    }

    @Transactional(readOnly = true)
    public FindChatRoomIdResponse findChatRoomId(Long volunteerId, Long shelterId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        Shelter shelter = getShelter(shelterId);

        Long chatRoomId = chatRoomRepository.findByVolunteerAndShelter(volunteer, shelter)
            .map(ChatRoom::getChatRoomId)
            .orElse(null);

        return new FindChatRoomIdResponse(chatRoomId);
    }

    @Transactional(readOnly = true)
    public FindUnreadCountResponse findUnreadCountByVolunteer(Long volunteerId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        long unreadCountByVolunteer = chatMessageRepository.findUnreadCountByVolunteer(volunteer);
        return FindUnreadCountResponse.from(unreadCountByVolunteer);
    }

    @Transactional(readOnly = true)
    public FindUnreadCountResponse findUnreadCountByShelter(Long shelterId) {
        Shelter shelter = getShelter(shelterId);
        long unreadCountByShelter = chatMessageRepository.findUnreadCountByShelter(shelter);
        return FindUnreadCountResponse.from(unreadCountByShelter);
    }

    @Transactional(readOnly = true)
    public FindChatRoomsResponse findChatRoomsByShelter(Long shelterId) {
        Shelter shelter = getShelter(shelterId);
        List<FindChatRoomResult> findChatRoomResults = chatRoomRepository.findChatRoomsByShelter(
            shelter);
        return ChatRoomMapper.resultToResponse(findChatRoomResults);
    }

    @Transactional(readOnly = true)
    public FindChatMessagesResponse findChatMessages(Long chatRoomId, Pageable pageable) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Page<ChatMessage> chatMessagePage
            = chatMessageRepository.findByChatRoomOrderByCreatedAtDesc(chatRoom, pageable);
        return FindChatMessagesResponse.from(chatMessagePage);
    }

    private ChatRoom getChatRoomWithVolunteer(Long chatRoomId) {
        return chatRoomRepository.findByIdWithVolunteer(chatRoomId)
            .orElseThrow(() -> new ChatNotFoundException("존재하지 않는 채팅방입니다."));
    }

    private ChatRoom getChatRoomWithShelter(Long chatRoomId) {
        return chatRoomRepository.findByIdWithShelter(chatRoomId)
            .orElseThrow(() -> new ChatNotFoundException("존재하지 않는 채팅방입니다."));
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatNotFoundException("존재하지 않는 채팅방입니다."));
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository
            .findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("보호소가 존재하지 않습니다."));
    }
}
