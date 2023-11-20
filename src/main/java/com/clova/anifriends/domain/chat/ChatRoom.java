package com.clova.anifriends.domain.chat;

import com.clova.anifriends.domain.chat.exception.ChatRoomBadRequestException;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "chat_room",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"volunteer_id", "shelter_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(Volunteer volunteer, Shelter shelter) {
        validateVolunteer(volunteer);
        validateShelter(shelter);
        this.volunteer = volunteer;
        this.shelter = shelter;
    }

    private void validateShelter(Shelter shelter) {
        if (Objects.isNull(shelter)) {
            throw new ChatRoomBadRequestException("채팅방의 보호소가 존재하지 않습니다.");
        }
    }

    private void validateVolunteer(Volunteer volunteer) {
        if (Objects.isNull(volunteer)) {
            throw new ChatRoomBadRequestException("채팅방의 봉사자가 존재하지 않습니다.");
        }
    }
}
