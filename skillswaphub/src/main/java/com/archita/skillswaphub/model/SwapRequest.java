package com.archita.skillswaphub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "swap_requests")
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;           // Who sent the request

    @Column(nullable = false)
    private Long receiverId;         // Who received the request

    @Column(nullable = false)
    private String senderName;       // Sender's name

    @Column(nullable = false)
    private String receiverName;     // Receiver's name

    @Column(nullable = false)
    private String skillOffered;     // What sender offers to teach

    @Column(nullable = false)
    private String skillWanted;      // What sender wants to learn

    @Column(columnDefinition = "TEXT")
    private String message;          // Optional message with the request

    @Column(nullable = false)
    private String status;           // PENDING, ACCEPTED, REJECTED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime respondedAt;

    // ===== CONSTRUCTORS =====
    public SwapRequest() {}

    public SwapRequest(Long senderId, Long receiverId, String senderName,
                       String receiverName, String skillOffered,
                       String skillWanted, String message) {
        this.senderId     = senderId;
        this.receiverId   = receiverId;
        this.senderName   = senderName;
        this.receiverName = receiverName;
        this.skillOffered = skillOffered;
        this.skillWanted  = skillWanted;
        this.message      = message;
        this.status       = "PENDING";
        this.createdAt    = LocalDateTime.now();
    }

    // ===== GETTERS =====
    public Long getId()                  { return id; }
    public Long getSenderId()            { return senderId; }
    public Long getReceiverId()          { return receiverId; }
    public String getSenderName()        { return senderName; }
    public String getReceiverName()      { return receiverName; }
    public String getSkillOffered()      { return skillOffered; }
    public String getSkillWanted()       { return skillWanted; }
    public String getMessage()           { return message; }
    public String getStatus()            { return status; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getRespondedAt(){ return respondedAt; }

    // ===== SETTERS =====
    public void setId(Long id)                       { this.id = id; }
    public void setSenderId(Long s)                  { this.senderId = s; }
    public void setReceiverId(Long r)                { this.receiverId = r; }
    public void setSenderName(String s)              { this.senderName = s; }
    public void setReceiverName(String r)            { this.receiverName = r; }
    public void setSkillOffered(String s)            { this.skillOffered = s; }
    public void setSkillWanted(String s)             { this.skillWanted = s; }
    public void setMessage(String m)                 { this.message = m; }
    public void setStatus(String status)             { this.status = status; }
    public void setCreatedAt(LocalDateTime t)        { this.createdAt = t; }
    public void setRespondedAt(LocalDateTime t)      { this.respondedAt = t; }
}