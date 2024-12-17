package com.example.medical.store.Prescription;


import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private UserRepository userRepository;

    private static final String BUCKET_NAME = "medicalstore";


    public ResponseEntity<?> uploadPrescription(long userId,Prescription prescription, MultipartFile image) {

        Prescription prescription1 = new Prescription();
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent()){

            prescription1.setUploadDate(LocalDate.now());
            prescription1.setDeliveryType(prescription.getDeliveryType());
            prescription1.setStatus(prescription.getStatus());
            prescription1.setUrgency(prescription.getUrgency());
        }
        return null;
    }
}
