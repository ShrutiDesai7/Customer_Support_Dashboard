package com.supportdesk.controller;

import com.supportdesk.model.Ticket;
import com.supportdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    /**
     * Create a new ticket
     * POST /tickets
     */
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Map<String, Object> request) {
        try {
            Long customerId = Long.parseLong(request.get("customerId").toString());
            String title = request.get("title").toString();
            String description = request.get("description").toString();
            String priority = request.get("priority") != null ? request.get("priority").toString() : "MEDIUM";

            Ticket.TicketPriority ticketPriority = Ticket.TicketPriority.valueOf(priority.toUpperCase());
            Ticket ticket = ticketService.createTicket(customerId, title, description, ticketPriority);

            Map<String, Object> response = new HashMap<>();
            response.put("id", ticket.getId());
            response.put("title", ticket.getTitle());
            response.put("description", ticket.getDescription());
            response.put("status", ticket.getStatus());
            response.put("priority", ticket.getPriority());
            response.put("customerId", ticket.getCustomer().getId());
            response.put("agentId", ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getId() : null);
            response.put("createdAt", ticket.getCreatedAt());
            response.put("message", "Ticket created successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all tickets for a customer
     * GET /tickets/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getTicketsByCustomer(@PathVariable Long customerId) {
        try {
            List<Ticket> tickets = ticketService.getTicketsByCustomer(customerId);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all tickets assigned to an agent
     * GET /tickets/agent/{agentId}
     */
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<?> getTicketsByAgent(@PathVariable Long agentId) {
        try {
            List<Ticket> tickets = ticketService.getTicketsByAgent(agentId);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get unassigned tickets
     * GET /tickets/unassigned
     */
    @GetMapping("/unassigned")
    public ResponseEntity<?> getUnassignedTickets() {
        try {
            List<Ticket> tickets = ticketService.getUnassignedTickets();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get ticket by ID
     * GET /tickets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        try {
            Optional<Ticket> ticket = ticketService.getTicketById(id);

            if (ticket.isPresent()) {
                return ResponseEntity.ok(ticket.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all unassigned tickets
     * GET /tickets/unassigned
     */
    @GetMapping("/unassigned")
    public ResponseEntity<?> getUnassignedTickets() {
        try {
            List<Ticket> tickets = ticketService.getUnassignedTickets();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all tickets
     * GET /tickets
     */
    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.getAllTickets();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Update ticket status
     * PATCH /tickets/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String status = request.get("status").toString();
            Long userId = Long.parseLong(request.get("userId").toString());
            Ticket.TicketStatus ticketStatus = Ticket.TicketStatus.valueOf(status.toUpperCase());
            Ticket updatedTicket = ticketService.updateTicketStatus(id, ticketStatus, userId);

            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Update ticket priority
     * PATCH /tickets/{id}/priority
     */
    @PatchMapping("/{id}/priority")
    public ResponseEntity<?> updateTicketPriority(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String priority = request.get("priority").toString();
            Long userId = Long.parseLong(request.get("userId").toString());
            Ticket.TicketPriority ticketPriority = Ticket.TicketPriority.valueOf(priority.toUpperCase());
            Ticket updatedTicket = ticketService.updateTicketPriority(id, ticketPriority, userId);

            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Assign ticket to an agent
     * PATCH /tickets/{id}/assign
     */
    @PatchMapping("/{id}/assign")
    public ResponseEntity<?> assignTicket(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            Long agentId = Long.parseLong(request.get("agentId"));
            Ticket assignedTicket = ticketService.assignTicketToAgent(id, agentId);

            return ResponseEntity.ok(assignedTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Unassign ticket from agent
     * PATCH /tickets/{id}/unassign
     */
    @PatchMapping("/{id}/unassign")
    public ResponseEntity<?> unassignTicket(@PathVariable Long id) {
        try {
            Ticket unassignedTicket = ticketService.unassignTicket(id);
            return ResponseEntity.ok(unassignedTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
