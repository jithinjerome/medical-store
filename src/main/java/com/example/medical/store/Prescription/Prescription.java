package com.example.medical.store.Prescription;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
