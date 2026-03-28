package com.helpdesk.controller;

import com.helpdesk.entity.Comment;
import com.helpdesk.entity.Ticket;
import com.helpdesk.service.CommentService;
import com.helpdesk.service.TicketService;
import com.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final UserService userService;
    private final TicketService ticketService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long ticketId,
                                              @RequestBody Comment comment,
                                              Authentication authentication) {
        comment.setTicket(ticketService.getTicketById(ticketId));
        comment.setAuthor(userService.getUserByEmail(authentication.getName()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(comment));
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(commentService.getCommentsByTicket(ticket));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long ticketId,
                                              @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}