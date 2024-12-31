package com.example.medical.store.Prescription;

import lombok.Data;

@Data
public class PrescriptionResponseDTO {

    private PrescriptionRequest prescriptionRequest;
    private String urgency;
    private String deliveryType;
    private String status;

    public PrescriptionResponseDTO(PrescriptionRequest prescriptionRequest,
                                   String urgency,
                                   String deliveryType,
                                   String status){
        this.prescriptionRequest = prescriptionRequest;
        this.deliveryType = deliveryType;
        this.urgency = urgency;
        this.status = status;
    }
}
