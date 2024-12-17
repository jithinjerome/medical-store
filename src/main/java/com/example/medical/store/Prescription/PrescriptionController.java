package com.example.medical.store.Prescription;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;


    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPrescription(@PathVariable("userId") long userId, @RequestPart("Prescription") Prescription prescription,
                                      @RequestPart("image") MultipartFile image){
        try{
            return prescriptionService.uploadPrescription(userId,prescription,image);
        }catch (Exception e){
            return new ResponseEntity<>("Error during prescription uploading "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
