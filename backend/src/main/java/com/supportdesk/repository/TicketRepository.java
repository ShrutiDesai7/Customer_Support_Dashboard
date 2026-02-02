package com.supportdesk.repository;

import com.supportdesk.model.Ticket;
import com.supportdesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Find all tickets for a specific customer
    List<Ticket> findByCustomerId(Long customerId);

    // Find all tickets assigned to a specific agent
    List<Ticket> findByAssignedAgentId(Long agentId);

    // Find unassigned tickets
    List<Ticket> findByAssignedAgentIsNull();

    // Find tickets by status
    List<Ticket> findByStatus(Ticket.TicketStatus status);

    // Find tickets by priority
    List<Ticket> findByPriority(Ticket.TicketPriority priority);

    // Find open tickets for a customer
    List<Ticket> findByCustomerIdAndStatus(Long customerId, Ticket.TicketStatus status);

    // Find all tickets assigned to an agent with a specific status
    List<Ticket> findByAssignedAgentIdAndStatus(Long agentId, Ticket.TicketStatus status);
}
