package com.clova.anifriends.domain.chat.repository;

import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr"
        + " join fetch cr.shelter"
        + " join fetch cr.shelter.image"
        + " where cr.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findByIdWithShelter(@Param("chatRoomId") Long chatRoomId);

    @Query("select cr from ChatRoom cr"
        + " join fetch cr.volunteer"
        + " join fetch cr.volunteer.image"
        + " where cr.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findByIdWithVolunteer(@Param("chatRoomId") Long chatRoomId);

    @Query("select"
        + " cr.chatRoomId as chatRoomId,"
        + " cm.message as chatRecentMessage,"
        + " s.name.name as chatPartnerName,"
        + " s.image.imageUrl as chatPartnerImageUrl,"
        + " cm.createdAt as createdAt,"
        + " (select count(cm3.chatMessageId) from ChatMessage cm3"
        + " where cm3.chatRoom = cr"
        + " and cm3.isRead = false"
        + " and cm3.senderRole = com.clova.anifriends.domain.auth.jwt.UserRole.ROLE_SHELTER)"
        + " from ChatMessage cm"
        + " join cm.chatRoom cr"
        + " join cr.shelter s"
        + " where cr.volunteer = :volunteer"
        + " and cm.createdAt = ("
        + "select max(cm2.createdAt) from ChatMessage cm2"
        + " where cm2.chatRoom = cr)")
    List<FindChatRoomResult> findChatRoomsByVolunteer(@Param("volunteer") Volunteer volunteer);

    @Query("select"
        + " cr.chatRoomId as chatRoomId,"
        + " cm.message as chatRecentMessage,"
        + " v.name.name as chatPartnerName,"
        + " v.image.imageUrl as chatPartnerImageUrl,"
        + " cm.createdAt as createdAt,"
        + " (select count(cm3.chatMessageId) from ChatMessage cm3"
        + " where cm3.chatRoom = cr"
        + " and cm3.isRead = false"
        + " and cm3.senderRole = com.clova.anifriends.domain.auth.jwt.UserRole.ROLE_VOLUNTEER)"
        + " from ChatMessage cm"
        + " join cm.chatRoom cr"
        + " join cr.volunteer v"
        + " where cr.shelter = :shelter"
        + " and cm.createdAt = ("
        + "select max(cm2.createdAt) from ChatMessage cm2"
        + " where cm2.chatRoom = cr)")
    List<FindChatRoomResult> findChatRoomsByShelter(@Param("shelter") Shelter shelter);

    Optional<ChatRoom> findByVolunteerAndShelter(Volunteer volunteer, Shelter shelter);
}
