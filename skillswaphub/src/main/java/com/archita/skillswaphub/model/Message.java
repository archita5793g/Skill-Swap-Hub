package com.archita.skillswaphub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String senderName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private boolean isRead;

    public Message() {}

    public Message(Long senderId, Long receiverId, String senderName, String content) {
        this.senderId   = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.content    = content;
        this.sentAt     = LocalDateTime.now();
        this.isRead     = false;
    }

    public Long getId()              { return id; }
    public Long getSenderId()        { return senderId; }
    public Long getReceiverId()      { return receiverId; }
    public String getSenderName()    { return senderName; }
    public String getContent()       { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isRead()          { return isRead; }

    public void setId(Long id)                   { this.id = id; }
    public void setSenderId(Long s)              { this.senderId = s; }
    public void setReceiverId(Long r)            { this.receiverId = r; }
    public void setSenderName(String s)          { this.senderName = s; }
    public void setContent(String c)             { this.content = c; }
    public void setSentAt(LocalDateTime t)       { this.sentAt = t; }
    public void setRead(boolean r)               { this.isRead = r; }
}