package com.example.medical.store.Prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/request")
public class PrescriptionRequestController {

    @Autowired
    private PrescriptionRequestService prescriptionRequestService;

    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    @PostMapping(path = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> prescriptionRequest(@RequestParam long prescriptionId, @RequestParam int storeId){
        boolean exists = prescriptionRequestRepository.existsByPrescriptionIdAndStoreId(prescriptionId,storeId);
        if (exists) {
            throw new IllegalArgumentException("A request for this prescription to the same store already exists.");
        }
        return prescriptionRequestService.prescriptionRequest(prescriptionId,storeId);
    }
}
