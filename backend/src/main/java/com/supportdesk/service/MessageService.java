package com.supportdesk.service;

import com.supportdesk.model.Message;
import com.supportdesk.model.Ticket;
import com.supportdesk.model.User;
import com.supportdesk.repository.MessageRepository;
import com.supportdesk.repository.TicketRepository;
import com.supportdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a reply message to a ticket
     */
    public Message addReply(Long ticketId, Long senderId, String content) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + senderId));

        Message message = new Message();
        message.setTicket(ticket);
        message.setSender(sender);
        message.setContent(content);
        message.setMessageType(Message.MessageType.REPLY);

        return messageRepository.save(message);
    }

    /**
     * Add an internal note (only visible to agents)
     */
    public Message addNote(Long ticketId, Long senderId, String content) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + senderId));

        if (!sender.getRole().equals(User.UserRole.AGENT)) {
            throw new RuntimeException("Only agents can add internal notes");
        }

        Message message = new Message();
        message.setTicket(ticket);
        message.setSender(sender);
        message.setContent(content);
        message.setMessageType(Message.MessageType.NOTE);

        return messageRepository.save(message);
    }

    /**
     * Get all messages for a ticket
     */
    public List<Message> getTicketMessages(Long ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }

    /**
     * Get all messages sent by a user
     */
    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findBySenderId(userId);
    }

    /**
     * Get message by ID
     */
    public Optional<Message> getMessageById(Long messageId) {
        return messageRepository.findById(messageId);
    }

    /**
     * Delete message (only for notes)
     */
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        if (!message.getMessageType().equals(Message.MessageType.NOTE)) {
            throw new RuntimeException("Only notes can be deleted");
        }

        messageRepository.deleteById(messageId);
    }

    /**
     * Create a status update message
     */
    public Message createStatusUpdateMessage(Long ticketId, String statusMessage) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        // Get system user or admin user for status updates
        // For now, we'll use the ticket's agent if available
        User sender = ticket.getAssignedAgent() != null ? ticket.getAssignedAgent() : ticket.getCustomer();

        Message message = new Message();
        message.setTicket(ticket);
        message.setSender(sender);
        message.setContent(statusMessage);
        message.setMessageType(Message.MessageType.STATUS_UPDATE);

        return messageRepository.save(message);
    }
}
