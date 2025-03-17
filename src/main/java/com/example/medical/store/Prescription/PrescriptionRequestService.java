package com.example.medical.store.Prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionRequestService {

    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;


    public ResponseEntity<?> prescriptionRequest(long prescriptionId, int storeId) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(prescriptionId);
        if(prescriptionOptional.isPresent()){
            Prescription prescription = prescriptionOptional.get();
            PrescriptionRequest prescriptionRequest = new PrescriptionRequest();
            prescriptionRequest.setPrescriptionId(prescriptionId);
            prescriptionRequest.setStoreId(storeId);
            prescriptionRequest.setRequestDate(LocalDate.now());

            PrescriptionRequest saved = prescriptionRequestRepository.save(prescriptionRequest);

            PrescriptionResponseDTO responseDTO = new PrescriptionResponseDTO();
            responseDTO.setPrescriptionId(prescriptionId);
            responseDTO.setRequestDate(saved.getRequestDate());
            responseDTO.setStoreId(saved.getStoreId());
            responseDTO.setStatus(prescription.getStatus());
            responseDTO.setUrgency(prescription.getUrgency());
            responseDTO.setDeliveryType(prescription.getDeliveryType());


            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }
        return new ResponseEntity<>("Prescription not found", HttpStatus.NOT_FOUND);
    }

    public List<PrescriptionRequest> getAllPrescriptions() {
        return prescriptionRequestRepository.findAll();
    }
}
