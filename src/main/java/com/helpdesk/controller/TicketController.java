package com.helpdesk.controller;

import com.helpdesk.dto.TicketRequest;
import com.helpdesk.entity.Ticket;
import com.helpdesk.entity.User;
import com.helpdesk.enums.Status;
import com.helpdesk.service.TicketService;
import com.helpdesk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketRequest request,
                                               Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request, user));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(ticketService.getTicketsByUser(user));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Ticket> assignTicket(@PathVariable Long id,
                                               @RequestBody Map<String, Long> body) {
        User agent = userService.getUserEntityById(body.get("agentId"));
        return ResponseEntity.ok(ticketService.assignTicket(id, agent));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Long id,
                                               @RequestBody Map<String, String> body) {
        Status status = Status.valueOf(body.get("status"));
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}