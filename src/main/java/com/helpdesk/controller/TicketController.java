package com.helpdesk.controller;

import com.helpdesk.dto.TicketRequest;
import com.helpdesk.entity.Ticket;
import com.helpdesk.entity.User;
import com.helpdesk.enums.Status;
import com.helpdesk.service.TicketService;
import com.helpdesk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final UserService userService;
    private final TicketService ticketService;

    @PostMapping
    public Ticket createTicket(@Valid @RequestBody TicketRequest request,
                               Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return ticketService.createTicket(request, user);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/my")
    public List<Ticket> getMyTickets(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return ticketService.getTicketsByUser(user);
    }

    @PutMapping("/{id}/assign")
    public Ticket assignTicket(@PathVariable Long id,
                               @RequestBody Map<String, Long> body) {
        User agent = userService.getUserEntityById(body.get("agentId"));
        return ticketService.assignTicket(id, agent);
    }

    @PutMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable Long id,
                               @RequestBody Map<String, String> body) {
        Status status = Status.valueOf(body.get("status"));
        return ticketService.updateTicketStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}