package org.example.sbappwithprofilesandconfigurations.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Username is required")
    private String username;
    @NotEmpty(message = "Action is required")
    private String action;
    @NotEmpty(message = "IpAddress is required")
    private String ipAddress;
    private final LocalDateTime timeStamp;


    public ActivityLog(String username, String action, String ipAddress) {
        this.username = username;
        this.action = action;
        this.ipAddress = ipAddress;
        this.timeStamp = LocalDateTime.now();
    }

    public ActivityLog() {
        this.timeStamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", action='" + action + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
