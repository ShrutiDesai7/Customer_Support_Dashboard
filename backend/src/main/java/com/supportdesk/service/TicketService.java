package com.supportdesk.service;

import com.supportdesk.model.Ticket;
import com.supportdesk.model.User;
import com.supportdesk.repository.TicketRepository;
import com.supportdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new ticket
     */
    public Ticket createTicket(Long customerId, String title, String description, Ticket.TicketPriority priority) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        if (!customer.getRole().equals(User.UserRole.CUSTOMER)) {
            throw new RuntimeException("Only customers can create tickets");
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCustomer(customer);
        ticket.setPriority(priority != null ? priority : Ticket.TicketPriority.MEDIUM);
        ticket.setStatus(Ticket.TicketStatus.OPEN);

        // Auto-assign ticket to an available agent (simple round-robin logic)
        assignTicketToAgent(ticket);

        return ticketRepository.save(ticket);
    }

    /**
     * Auto-assign ticket to agent with least open tickets (load balancing)
     */
    private void assignTicketToAgent(Ticket ticket) {
        List<User> activeAgents = userRepository.findByRoleAndActive(User.UserRole.AGENT, true);

        if (!activeAgents.isEmpty()) {
            // Find agent with least open tickets (load balancing)
            User leastBusyAgent = activeAgents.stream()
                    .min((a, b) -> {
                        long aTicketCount = ticketRepository.findByAssignedAgentIdAndStatus(a.getId(), Ticket.TicketStatus.OPEN).size() +
                                ticketRepository.findByAssignedAgentIdAndStatus(a.getId(), Ticket.TicketStatus.IN_PROGRESS).size();
                        long bTicketCount = ticketRepository.findByAssignedAgentIdAndStatus(b.getId(), Ticket.TicketStatus.OPEN).size() +
                                ticketRepository.findByAssignedAgentIdAndStatus(b.getId(), Ticket.TicketStatus.IN_PROGRESS).size();
                        return Long.compare(aTicketCount, bTicketCount);
                    })
                    .orElse(activeAgents.get(0));
            ticket.setAssignedAgent(leastBusyAgent);
        }
    }

    /**
     * Get ticket by ID
     */
    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    /**
     * Get all tickets for a customer
     */
    public List<Ticket> getTicketsByCustomer(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    /**
     * Get all tickets assigned to an agent
     */
    public List<Ticket> getTicketsByAgent(Long agentId) {
        return ticketRepository.findByAssignedAgentId(agentId);
    }

    /**
     * Get all unassigned tickets
     */
    public List<Ticket> getUnassignedTickets() {
        return ticketRepository.findByAssignedAgentIsNull();
    }

    /**
     * Get all tickets
     */
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Update ticket status (only agents can update status)
     */
    public Ticket updateTicketStatus(Long ticketId, Ticket.TicketStatus status, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Only agents can update ticket status
        if (!user.getRole().equals(User.UserRole.AGENT)) {
            throw new RuntimeException("Only agents can update ticket status");
        }

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    /**
     * Update ticket priority (only agents can update priority)
     */
    public Ticket updateTicketPriority(Long ticketId, Ticket.TicketPriority priority, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Only agents can update ticket priority
        if (!user.getRole().equals(User.UserRole.AGENT)) {
            throw new RuntimeException("Only agents can update ticket priority");
        }

        ticket.setPriority(priority);
        return ticketRepository.save(ticket);
    }

    /**
     * Assign ticket to an agent
     */
    public Ticket assignTicketToAgent(Long ticketId, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + agentId));

        if (!agent.getRole().equals(User.UserRole.AGENT)) {
            throw new RuntimeException("User is not an agent");
        }

        ticket.setAssignedAgent(agent);
        return ticketRepository.save(ticket);
    }

    /**
     * Unassign ticket from agent
     */
    public Ticket unassignTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        ticket.setAssignedAgent(null);
        return ticketRepository.save(ticket);
    }

    /**
     * Get open tickets for a customer
     */
    public List<Ticket> getOpenTicketsByCustomer(Long customerId) {
        return ticketRepository.findByCustomerIdAndStatus(customerId, Ticket.TicketStatus.OPEN);
    }

    /**
     * Get in-progress tickets assigned to an agent
     */
    public List<Ticket> getInProgressTicketsByAgent(Long agentId) {
        return ticketRepository.findByAssignedAgentIdAndStatus(agentId, Ticket.TicketStatus.IN_PROGRESS);
    }
}
