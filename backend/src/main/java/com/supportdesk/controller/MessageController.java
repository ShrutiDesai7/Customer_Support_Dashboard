package com.supportdesk.controller;

import com.supportdesk.model.Message;
import com.supportdesk.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * Add a reply message to a ticket
     * POST /messages/reply
     */
    @PostMapping("/reply")
    public ResponseEntity<?> addReply(@RequestBody Map<String, Object> request) {
        try {
            Long ticketId = Long.parseLong(request.get("ticketId").toString());
            Long senderId = Long.parseLong(request.get("senderId").toString());
            String content = request.get("content").toString();

            Message message = messageService.addReply(ticketId, senderId, content);

            Map<String, Object> response = new HashMap<>();
            response.put("id", message.getId());
            response.put("ticketId", message.getTicket().getId());
            response.put("senderId", message.getSender().getId());
            response.put("senderName", message.getSender().getFullName());
            response.put("content", message.getContent());
            response.put("messageType", message.getMessageType());
            response.put("createdAt", message.getCreatedAt());
            response.put("message", "Reply added successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add an internal note to a ticket
     * POST /messages/note
     */
    @PostMapping("/note")
    public ResponseEntity<?> addNote(@RequestBody Map<String, Object> request) {
        try {
            Long ticketId = Long.parseLong(request.get("ticketId").toString());
            Long senderId = Long.parseLong(request.get("senderId").toString());
            String content = request.get("content").toString();

            Message message = messageService.addNote(ticketId, senderId, content);

            Map<String, Object> response = new HashMap<>();
            response.put("id", message.getId());
            response.put("ticketId", message.getTicket().getId());
            response.put("senderId", message.getSender().getId());
            response.put("senderName", message.getSender().getFullName());
            response.put("content", message.getContent());
            response.put("messageType", message.getMessageType());
            response.put("createdAt", message.getCreatedAt());
            response.put("message", "Note added successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all messages for a ticket
     * GET /messages/ticket/{ticketId}
     */
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<?> getTicketMessages(@PathVariable Long ticketId) {
        try {
            List<Message> messages = messageService.getTicketMessages(ticketId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all messages sent by a user
     * GET /messages/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMessages(@PathVariable Long userId) {
        try {
            List<Message> messages = messageService.getUserMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get message by ID
     * GET /messages/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Long id) {
        try {
            Optional<Message> message = messageService.getMessageById(id);

            if (message.isPresent()) {
                return ResponseEntity.ok(message.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Delete a message (only for notes)
     * DELETE /messages/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.ok("Message deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
