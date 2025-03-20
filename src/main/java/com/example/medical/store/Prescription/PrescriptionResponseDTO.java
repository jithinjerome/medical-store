package com.example.medical.store.Prescription;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Data
public class PrescriptionResponseDTO {

    private String urgency;
    private String deliveryType;
    private String status;
    private long prescriptionId;
    private long storeId;
    private LocalDate requestDate;

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrescriptionId(long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

}
