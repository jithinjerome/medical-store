package com.example.medical.store.Prescription;

import com.example.medical.store.NotificationSystem.Notification.NotificationService;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;


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

    public List<PrescriptionRequest> getAllPrescriptions() {
        return prescriptionRequestRepository.findAll();
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
