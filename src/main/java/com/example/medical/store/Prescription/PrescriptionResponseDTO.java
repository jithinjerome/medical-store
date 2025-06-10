package com.example.medical.store.Prescription;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrescriptionResponseDTO {

    @JsonProperty("urgency")
    private String urgency;
    @JsonProperty("deliveryType")
    private String deliveryType;
    @JsonProperty("status")
    private String status;
    @JsonProperty("prescriptionId")
    private long prescriptionId;
    @JsonProperty("storeId")
    private long storeId;
    @JsonProperty("requestDate")
    private LocalDate requestDate;

    @JsonProperty("imageUrl")
    private String imageUrl;
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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
