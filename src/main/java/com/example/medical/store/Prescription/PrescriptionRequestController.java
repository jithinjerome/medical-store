package com.example.medical.store.Prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/request")
public class PrescriptionRequestController {

    @Autowired
    private PrescriptionRequestService prescriptionRequestService;

    @PostMapping(path = "/send")
    public ResponseEntity<?> prescriptionRequest(@RequestParam long prescriptionId, @RequestParam long storeId){
        return prescriptionRequestService.prescriptionRequest(prescriptionId,storeId);
    }
}
