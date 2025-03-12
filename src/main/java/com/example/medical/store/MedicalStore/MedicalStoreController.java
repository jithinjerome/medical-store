package com.example.medical.store.MedicalStore;

import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.Admin.AdminModel;
import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestService;
import com.example.medical.store.StoreEmployee.StoreEmployee;
import com.example.medical.store.StoreEmployee.StoreEmployeeDTO;
import com.example.medical.store.StoreEmployee.StoreEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/auth/medical-store")
public class MedicalStoreController {

    @Autowired
    private MedicalStoreService medicalStoreService;

    @Autowired
    private StoreEmployeeService storeEmployeeService;

    @Autowired
    private PrescriptionRequestService prescriptionRequestService;



    @PostMapping("/register")
    public ResponseEntity<?> medicalStoreRegister(@RequestPart("medicalStoreModel") MedicalStoreModel medicalStoreModel,
                                                  @RequestPart("licenseImage") MultipartFile licenseImage)  {
        try{
            MedicalStoreModel registeredMedicalStore = medicalStoreService.registerMedicalStore(medicalStoreModel,licenseImage);
            return new ResponseEntity<>(medicalStoreService.convertToDTO(registeredMedicalStore), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> medicalStoreLogin(@RequestBody MedicalStoreModel medicalStoreModel) {
        try {
            String loginResponse = medicalStoreService.medicalStoreLogin(medicalStoreModel.getEmail(), medicalStoreModel.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path ="/addEmployee")
    public ResponseEntity<?> addEmployee(@RequestBody StoreEmployeeDTO storeEmployeeDTO){
        try {
            StoreEmployeeDTO addedEmployee = storeEmployeeService.addEmployee(storeEmployeeDTO);
            return new ResponseEntity<>(addedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path ="/updateEmployee/{id}")
    public ResponseEntity<String> updateEmployee(){
        try {


        }catch (Exception e){

        }
    }
    @DeleteMapping(path ="/removeEmployee/{id}")
    public ResponseEntity<String> removeEmployee(@PathVariable Long id){
        try{
            storeEmployeeService.removeEmployee(id);
            return new ResponseEntity<>("Employee Deleted",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Employee not Found",HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(path ="/allPrescriptions")
    public ResponseEntity<List<PrescriptionRequest>> allPrescriptionRequests(){
        List<PrescriptionRequest> prescriptions = prescriptionRequestService.getAllPrescriptions();
        return new ResponseEntity<>(prescriptions,HttpStatus.OK);
    }
    @GetMapping(path ="/allEmployees")
    public ResponseEntity<List<StoreEmployeeDTO>> allEmployees(){
        List<StoreEmployeeDTO> storeEmployees = storeEmployeeService.getAllEmployees();
        return new ResponseEntity<>(storeEmployees,HttpStatus.OK);
    }
}
