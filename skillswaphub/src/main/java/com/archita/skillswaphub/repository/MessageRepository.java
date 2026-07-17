package com.archita.skillswaphub.repository;

import com.archita.skillswaphub.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Get full conversation between two users
    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderId = :u1 AND m.receiverId = :u2) OR " +
            "(m.senderId = :u2 AND m.receiverId = :u1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findConversation(@Param("u1") Long u1, @Param("u2") Long u2);

    // Count unread messages for a user
    long countByReceiverIdAndIsRead(Long receiverId, boolean isRead);

    // Get unique chat partner IDs for a user
    @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END " +
            "FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId")
    List<Long> findChatPartnerIds(@Param("userId") Long userId);
}