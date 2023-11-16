package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final VolunteerRepository volunteerRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public FindChatRoomsResponse findChatRoomsByVolunteer(Long volunteerId) {
        Volunteer volunteer = getVolunteer(volunteerId);
        List<FindChatRoomResult> findChatRoomResult
            = chatRoomRepository.findChatRoomsByVolunteer(volunteer);
        return ChatRoomMapper.toResponse(findChatRoomResult);
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }
}
