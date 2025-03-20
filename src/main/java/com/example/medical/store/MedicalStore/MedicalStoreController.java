package com.example.medical.store.MedicalStore;

import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestService;
import com.example.medical.store.Prescription.PrescriptionResponseDTO;
import com.example.medical.store.StoreEmployee.StoreEmployee;
import com.example.medical.store.StoreEmployee.StoreEmployeeDTO;
import com.example.medical.store.StoreEmployee.StoreEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/auth/medical-store")
public class MedicalStoreController {

    @Autowired
    private MedicalStoreService medicalStoreService;


    private MedicalStoreDTO medicalStoreDTO;

    @Autowired
    private StoreEmployeeService storeEmployeeService;

    @Autowired
    private PrescriptionRequestService prescriptionRequestService;

    @PostMapping("/register")
    public ResponseEntity<?> medicalStoreRegister(@RequestPart("medicalStoreModel") MedicalStoreDTO medicalStoreDTO,
                                                  @RequestPart("storeLicenseImage") MultipartFile storeLicenseImage) {
        try {
            MedicalStoreDTO registeredMedicalStore = medicalStoreService.registerMedicalStore(medicalStoreDTO, storeLicenseImage);
            return new ResponseEntity<>(registeredMedicalStore, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> medicalStoreLogin(@RequestBody MedicalStoreDTO medicalStoreDTO) {
        try {
            String loginResponse = medicalStoreService.medicalStoreLogin(medicalStoreDTO.getEmail(), medicalStoreDTO.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping(path = "/allPrescriptions/{storeId}")
//    public ResponseEntity<List<PrescriptionRequest>> allRequests(@PathVariable int storeId){
//        return medicalStoreService.allPrescriptions(storeId);
//    }

    @PostMapping("/addEmployee")
    public ResponseEntity<?> addEmployee(@RequestBody StoreEmployeeDTO storeEmployeeDTO) {
        try {
            StoreEmployee addedEmployee = storeEmployeeService.addEmployee(storeEmployeeDTO);
            return new ResponseEntity<>(addedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody StoreEmployeeDTO storeEmployeeDTO) {
        try {
            StoreEmployeeDTO updatedEmployee = storeEmployeeService.updateEmployee(id, storeEmployeeDTO);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/removeEmployee/{id}")
    public ResponseEntity<String> removeEmployee(@PathVariable Long id) {
        try {
            storeEmployeeService.removeEmployee(id);
            return new ResponseEntity<>("Employee Deleted", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Employee not Found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/allPrescriptions/{storeId}")
    public ResponseEntity<List<PrescriptionResponseDTO>> allPrescriptionRequests(@PathVariable int storeId) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionRequestService.getAllPrescriptionsForStore(storeId);
        if(prescriptions.isEmpty()){
            return ResponseEntity.noContent().build();
        }else {
            return  ResponseEntity.ok(prescriptions);
        }
    }


    @GetMapping("/allEmployees")
    public ResponseEntity<List<StoreEmployeeDTO>> allEmployees() {
        List<StoreEmployeeDTO> storeEmployees = storeEmployeeService.getAllEmployees();
        return new ResponseEntity<>(storeEmployees, HttpStatus.OK);
    }

}
