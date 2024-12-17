package com.example.medical.store.Prescription;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;
    private LocalDate uploadDate;
    private byte[] image;
    private String  urgency;
    private String deliveryType;
    private String status;
}
