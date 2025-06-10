package com.example.medical.store.NotificationSystem.Notification;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String message;
    private String type;
    private long userId;
    private int storeId;

    private boolean isRead = false;


    private LocalDateTime timestamp = LocalDateTime.now();

    @PrePersist
    public void onCreate(){
        this.timestamp = LocalDateTime.now();
    }
}
