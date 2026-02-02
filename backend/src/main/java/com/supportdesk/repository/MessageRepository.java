package com.supportdesk.repository;

import com.supportdesk.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Find all messages for a specific ticket
    List<Message> findByTicketId(Long ticketId);

    // Find all messages sent by a specific user
    List<Message> findBySenderId(Long senderId);

    // Find messages by type
    List<Message> findByMessageType(Message.MessageType messageType);
}
