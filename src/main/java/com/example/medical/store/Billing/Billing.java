package com.example.medical.store.Billing;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data
@Table(name = "Billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;
    private long prescriptionId;
    private int storeId;
    private String storeName;
    private String deliveryType;

    @ElementCollection
    private List<String> medicines;

    private BigDecimal totalMedicinePrice;
    private BigDecimal gst;
    private BigDecimal deliveryCharges;
    private BigDecimal totalCharges;

}
