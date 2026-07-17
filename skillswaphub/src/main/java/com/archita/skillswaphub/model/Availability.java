package com.archita.skillswaphub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    private String note;

    // Default Constructor
    public Availability() {
    }

    // Parameterized Constructor
    public Availability(Long userId, String day, String startTime, String endTime, String note) {
        this.userId = userId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.note = note;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getNote() {
        return note;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNote(String note) {
        this.note = note;
    }
}