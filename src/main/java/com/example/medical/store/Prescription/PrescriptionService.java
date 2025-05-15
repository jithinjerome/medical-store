package com.example.medical.store.Prescription;


import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private static final String BUCKET_NAME = "medicalstore";


    @Transactional
    public ResponseEntity<?> uploadPrescription(long userId, Prescription prescription, MultipartFile image) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }

        try {
            Prescription prescription1 = new Prescription();
            prescription1.setUserId(userId);
            prescription1.setUploadDate(LocalDate.now());
            prescription1.setDeliveryType(prescription.getDeliveryType());
            prescription1.setStatus(prescription.getStatus());
            prescription1.setUrgency(prescription.getUrgency());

            if (image == null || image.isEmpty()) {
                return new ResponseEntity<>("Image is required", HttpStatus.BAD_REQUEST);
            }

            String keyName = "prescriptions/" + userId + "_" + image.getOriginalFilename();
            String imageURL = fileUploadService.uploadFile(BUCKET_NAME, keyName, image.getBytes());
            prescription1.setImageURL(imageURL);

            prescriptionRepository.save(prescription1);
            return new ResponseEntity<>("Prescription uploaded successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error in uploading the image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Prescription>> prescriptionById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            List<Prescription> prescriptions = prescriptionRepository.findByUserId(userId);
            return new ResponseEntity<>(prescriptions,HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
    }

    @Transactional //Dynamic Update
    public ResponseEntity<?> updatePrescription(long id, Prescription prescription) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if(prescriptionOptional.isPresent()){

            Prescription updatedPrescription = prescriptionOptional.get();


//            if(prescription == null){
//                return new ResponseEntity<>("Prescription details are required", HttpStatus.BAD_REQUEST);
//            }

            String oldDeliveryType = updatedPrescription.getDeliveryType();
            String oldUrgency = updatedPrescription.getUrgency();
            String oldStatus = updatedPrescription.getStatus();


            if(prescription.getUrgency() != null && !prescription.getUrgency().equals(updatedPrescription.getUrgency())){
                updatedPrescription.setUrgency(prescription.getUrgency());
            }
            if(prescription.getStatus() != null && !prescription.getStatus().equals(updatedPrescription.getStatus())){
                updatedPrescription.setStatus(prescription.getStatus());
            }
            if(prescription.getDeliveryType() != null && !prescription.getDeliveryType().equals(updatedPrescription.getDeliveryType())){
                updatedPrescription.setDeliveryType(prescription.getDeliveryType());
            }

            Prescription saved = prescriptionRepository.save(updatedPrescription);

            Prescription response = new Prescription();
            response.setId(saved.getId());
            response.setUserId(saved.getUserId());
            response.setUploadDate(saved.getUploadDate());

            response.setUrgency(updatedPrescription.getUrgency() != null ? updatedPrescription.getUrgency() : oldUrgency);
            response.setStatus(updatedPrescription.getStatus() != null ? updatedPrescription.getStatus() : oldStatus);
            response.setDeliveryType(updatedPrescription.getDeliveryType() != null ? updatedPrescription.getDeliveryType() : oldDeliveryType);


            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        return new ResponseEntity<>("Prescription not found",HttpStatus.NOT_FOUND);
    }

    public void deletePrescription(long id) {
        prescriptionRepository.deleteById(id);
    }
}
