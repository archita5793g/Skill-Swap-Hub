package com.archita.skillswaphub.controller;

import com.archita.skillswaphub .model.Message;
import com.archita.skillswaphub.model.User;
import com.archita.skillswaphub.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // POST /api/messages/send
    // Body: { senderId, receiverId, content }
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody Map<String, Object> body) {
        try {
            Long   senderId   = Long.valueOf(body.get("senderId").toString());
            Long   receiverId = Long.valueOf(body.get("receiverId").toString());
            String content    = body.get("content").toString();
            Message msg = messageService.sendMessage(senderId, receiverId, content);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/messages/conversation?user1=1&user2=2
    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(@RequestParam Long user1, @RequestParam Long user2) {
        return ResponseEntity.ok(messageService.getConversation(user1, user2));
    }

    // GET /api/messages/partners/{userId}
    @GetMapping("/partners/{userId}")
    public ResponseEntity<?> getPartners(@PathVariable Long userId) {
        List<User> partners = messageService.getChatPartners(userId);
        return ResponseEntity.ok(partners);
    }

    // GET /api/messages/unread/{userId}
    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> unreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("unread", messageService.getUnreadCount(userId)));
    }

    // PUT /api/messages/read?sender=1&receiver=2
    @PutMapping("/read")
    public ResponseEntity<?> markRead(@RequestParam Long sender, @RequestParam Long receiver) {
        messageService.markAsRead(sender, receiver);
        return ResponseEntity.ok(Map.of("message", "Marked as read."));
    }

    // DELETE /api/messages/conversation?user1=1&user2=2
    @DeleteMapping("/conversation")
    public ResponseEntity<?> deleteConversation(@RequestParam Long user1, @RequestParam Long user2) {
        messageService.deleteConversation(user1, user2);
        return ResponseEntity.ok(Map.of("message", "Conversation deleted."));
    }
}