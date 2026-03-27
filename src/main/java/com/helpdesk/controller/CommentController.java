package com.helpdesk.controller;

import com.helpdesk.entity.Comment;
import com.helpdesk.entity.Ticket;
import com.helpdesk.service.CommentService;
import com.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final TicketService ticketService;
    private final CommentService commentService;

    @PostMapping
    public Comment addComment(@PathVariable Long ticketId, @RequestBody Comment comment) {
        comment.setTicket(ticketService.getTicketById(ticketId));
        return commentService.addComment(comment);
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return commentService.getCommentsByTicket(ticket);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long ticketId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}