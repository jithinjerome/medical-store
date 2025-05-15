package com.example.medical.store.Prescription;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;


//    @PostMapping(path = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadPrescription(@PathVariable("userId") long userId,
//                                                @RequestPart("Prescription") Prescription prescription,
//                                                @RequestPart("image") MultipartFile image){
//        try{
//            return prescriptionService.uploadPrescription(userId,prescription,image);
//        }catch (Exception e){
//            return new ResponseEntity<>("Error during prescription uploading "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping(path = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPrescription(@PathVariable("userId") long userId,
                                                @RequestParam("Prescription") String prescriptionJson,
                                                @RequestPart("image") MultipartFile image){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Prescription prescription = objectMapper.readValue(prescriptionJson, Prescription.class);

            return prescriptionService.uploadPrescription(userId, prescription, image);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during prescription upload: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/{userId}")
    public ResponseEntity<List<Prescription>> prescriptionById(@PathVariable("userId") long userId){
        return prescriptionService.prescriptionById(userId);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable long id,
                                                @RequestBody Prescription prescription){
        return prescriptionService.updatePrescription(id,prescription);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable long id){
        prescriptionService.deletePrescription(id);
        return new ResponseEntity<>("Prescription deleted successfully",HttpStatus.OK);
    }
}
