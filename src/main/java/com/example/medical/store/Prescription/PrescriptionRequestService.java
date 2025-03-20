package com.example.medical.store.Prescription;

import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionRequestService {

    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;


    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

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

    public List<PrescriptionResponseDTO> getAllPrescriptionsForStore(int storeId) {
        if(!medicalStoreRepo.existsById(storeId)){
            System.out.println("Store with ID " + storeId + " not found!");
            return new ArrayList<>();//returns empty arraylist
        }

        List<PrescriptionRequest> prescriptionRequests = prescriptionRequestRepository.findByStoreId(storeId);
        List<PrescriptionResponseDTO> responseDTOs = new ArrayList<>();

        for (PrescriptionRequest request : prescriptionRequests) {
            Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(request.getPrescriptionId());

            if (prescriptionOpt.isPresent()) {
                Prescription prescription = prescriptionOpt.get();

                PrescriptionResponseDTO dto = new PrescriptionResponseDTO();
                dto.setPrescriptionId(prescription.getId());
                dto.setStoreId(storeId);
                dto.setRequestDate(request.getRequestDate());
                dto.setUrgency(prescription.getUrgency());
                dto.setDeliveryType(prescription.getDeliveryType());
                dto.setStatus(prescription.getStatus());

                responseDTOs.add(dto);
            }
        }

        return responseDTOs;
    }

}
