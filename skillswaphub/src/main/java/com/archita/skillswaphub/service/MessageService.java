package com.archita.skillswaphub.service;

import com.archita.skillswaphub.model.Message;
import com.archita.skillswaphub.model.User;
import com.archita.skillswaphub.repository.MessageRepository;
import com.archita.skillswaphub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // Send a message
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        if (content == null || content.trim().isEmpty())
            throw new RuntimeException("Message cannot be empty.");

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found."));
        userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found."));

        return messageRepository.save(
                new Message(senderId, receiverId, sender.getName(), content.trim())
        );
    }

    // Get conversation between two users
    public List<Message> getConversation(Long u1, Long u2) {
        return messageRepository.findConversation(u1, u2);
    }

    // Get all users this person has chatted with
    public List<User> getChatPartners(Long userId) {
        List<Long> ids = messageRepository.findChatPartnerIds(userId);
        List<User> partners = new ArrayList<>();
        for (Long id : ids) {
            userRepository.findById(id).ifPresent(partners::add);
        }
        return partners;
    }

    // Get unread count
    public long getUnreadCount(Long userId) {
        return messageRepository.countByReceiverIdAndIsRead(userId, false);
    }

    // Mark messages as read
    public void markAsRead(Long senderId, Long receiverId) {
        List<Message> msgs = messageRepository.findConversation(senderId, receiverId);
        for (Message m : msgs) {
            if (m.getReceiverId().equals(receiverId) && !m.isRead()) {
                m.setRead(true);
                messageRepository.save(m);
            }
        }
    }

    // Delete a message
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    // Delete full conversation
    public void deleteConversation(Long u1, Long u2) {
        messageRepository.deleteAll(messageRepository.findConversation(u1, u2));
    }
}