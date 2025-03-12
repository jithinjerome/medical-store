package com.example.medical.store.Admin;

import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreService;
import com.example.medical.store.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MedicalStoreService medicalStoreService;

    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminModel loginRequest) {
        try {
            String loginResponse = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/medical-stores")
    public ResponseEntity<List<MedicalStoreModel>> getAllMedicalStores() {
        try {
            List<MedicalStoreModel> medicalstores = adminService.getAllMedicalStores();
            return new ResponseEntity<>(medicalstores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/delivery-persons")
    public ResponseEntity<List<DeliveryPersonModel>> getAllDeliveryPersons() {
        try {
            List<DeliveryPersonModel> deliverypersons = adminService.getAllDeliveryPersons();
            return new ResponseEntity<>(deliverypersons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/verifyMedicalStore/{id}")
    public ResponseEntity<?> verifyMedicalStore(@PathVariable int id) {
        try {
            MedicalStoreModel verifyStore = adminService.verifiedStore(id);
            return new ResponseEntity<>(verifyStore, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/revokeMedicalStore/{id}")
    public ResponseEntity<?> revokeVerifyStore(@PathVariable int id) {
        try {
            MedicalStoreModel verifiedStore = adminService.revokeVerifiedStore(id);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/verifyDeliveryPerson/{id}")
    public ResponseEntity<?> verifyDeliveryPerson(@PathVariable int id) {
        try {
            DeliveryPersonModel verifyDeliveryPerson = adminService.verifyDeliveryPerson(id);
            return new ResponseEntity<>(verifyDeliveryPerson, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/revokeDeliveryPerson/{id}")
    public ResponseEntity<?> revokeDeliveryPerson(@PathVariable int id) {
        try {
            DeliveryPersonModel revokeDeliveryPerson = adminService.revokeDeliveryPerson(id);
            return new ResponseEntity<>(revokeDeliveryPerson, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/removeDeliveryPerson/{id}")
    public ResponseEntity<?> removeDeliveryPerson(@PathVariable int id) {
        try {
            adminService.removeDeliveryPerson(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/removeStore/{id}")
    public ResponseEntity<?> removeStore(@PathVariable int id) {
        try {
            adminService.removeStore(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
