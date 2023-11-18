package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomIdResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.exception.ChatNotFoundException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.exception.ShelterNotFoundException;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final VolunteerRepository volunteerRepository;
    private final ShelterRepository shelterRepository;

    @Transactional(readOnly = true)
    public FindChatRoomDetailResponse findChatRoomDetailByVolunteer(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoomWithShelter(chatRoomId);
        chatMessageRepository.readPartnerMessages(chatRoom, UserRole.ROLE_VOLUNTEER);
        return FindChatRoomDetailResponse.fromVolunteer(chatRoom);
    }

    @Transactional
    public Long registerChatRoom(Long volunteerId, Long shelterId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        Shelter shelter = getShelter(shelterId);

        ChatRoom chatRoom = new ChatRoom(volunteer, shelter);
        chatRoomRepository.save(chatRoom);

        return chatRoom.getChatRoomId();
    }

    @Transactional(readOnly = true)
    public FindChatRoomsResponse findChatRoomsByVolunteer(Long volunteerId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        List<FindChatRoomResult> findChatRoomResult
            = chatRoomRepository.findChatRoomsByVolunteer(volunteer);
        return ChatRoomMapper.toResponse(findChatRoomResult);
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

    private ChatRoom getChatRoomWithShelter(Long chatRoomId) {
        return chatRoomRepository.findByIdWithShelter(chatRoomId)
            .orElseThrow(() -> new ChatNotFoundException("존재하지 않는 채팅방입니다."));
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }

    private Shelter getShelter(Long shelterId) {
        return shelterRepository
            .findById(shelterId)
            .orElseThrow(() -> new ShelterNotFoundException("보호소가 존재하지 않습니다."));
    }
}
