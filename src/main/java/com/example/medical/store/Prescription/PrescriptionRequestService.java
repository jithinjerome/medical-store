package com.example.medical.store.Prescription;

import com.example.medical.store.NotificationSystem.Notification.NotificationService;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    public ResponseEntity<?> prescriptionRequest(long prescriptionId, int storeId) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(prescriptionId);
        if(prescriptionOptional.isPresent()){
            Prescription prescription = prescriptionOptional.get();
            PrescriptionRequest prescriptionRequest = new PrescriptionRequest();
            prescriptionRequest.setPrescriptionId(prescriptionId);
            prescriptionRequest.setStoreId(storeId);
            prescriptionRequest.setUserId(prescription.getUserId());
            prescriptionRequest.setRequestDate(LocalDate.now());

            PrescriptionRequest saved = prescriptionRequestRepository.save(prescriptionRequest);

            notificationService.notifyUser(prescription.getUserId(), "Your prescription has been submitted successfully", "PRESCRIPTION SENT");
            notificationService.notifyStore(storeId, "You have a new prescription request", "PRESCRIPTION RECEIVED");

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
        System.out.println("✅ Found " + prescriptionRequests.size() + " prescription requests for store ID " + storeId);
        List<PrescriptionResponseDTO> responseDTOs = new ArrayList<>();

        for (PrescriptionRequest request : prescriptionRequests) {
            Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(request.getPrescriptionId());

            if (prescriptionOpt.isPresent()) {
                Prescription prescription = prescriptionOpt.get();
                System.out.println("📄 Prescription ID " + prescription.getId() + " found for store " + storeId);

                PrescriptionResponseDTO dto = new PrescriptionResponseDTO();
                dto.setPrescriptionId(prescription.getId());
                dto.setStoreId(storeId);
                dto.setRequestDate(request.getRequestDate());
                dto.setUrgency(prescription.getUrgency());
                dto.setDeliveryType(prescription.getDeliveryType());
                dto.setStatus(prescription.getStatus());
                dto.setImageUrl(prescription.getImageURL());

                responseDTOs.add(dto);
            }
            else {
                System.out.println("❌ Prescription Not Found for ID: " + request.getPrescriptionId());
            }
        }
        System.out.println("✅ Final responseDTOs size: " + responseDTOs.size());
        return responseDTOs;
    }

    public ResponseEntity<List<PrescriptionRequest>> getAllRequests(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PrescriptionRequest> requests = prescriptionRequestRepository.findByUserId(userId);
        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(requests,HttpStatus.OK);
    }

}
