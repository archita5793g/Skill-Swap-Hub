package com.archita.skillswaphub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    // ===== PRIMARY KEY =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== LOGIN FIELDS =====
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ===== PROFILE FIELDS =====
    @Column(columnDefinition = "TEXT")
    private String skills;       // Skills user can TEACH (comma separated)

    @Column(columnDefinition = "TEXT")
    private String wantToLearn;  // Skills user wants to LEARN (comma separated)

    @Column(columnDefinition = "TEXT")
    private String bio;          // Short bio about the user

    private String location;     // City / Country

    // ===== CONSTRUCTORS =====
    public User() {}

    public User(String name, String email, String password) {
        this.name     = name;
        this.email    = email;
        this.password = password;
    }

    // ===== GETTERS =====
    public Long getId()            { return id; }
    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getPassword()    { return password; }
    public String getSkills()      { return skills; }
    public String getWantToLearn() { return wantToLearn; }
    public String getBio()         { return bio; }
    public String getLocation()    { return location; }

    // ===== SETTERS =====
    public void setId(Long id)               { this.id = id; }
    public void setName(String name)         { this.name = name; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setSkills(String skills)     { this.skills = skills; }
    public void setWantToLearn(String w)     { this.wantToLearn = w; }
    public void setBio(String bio)           { this.bio = bio; }
    public void setLocation(String location) { this.location = location; }
}