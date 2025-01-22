package com.example.medical.store.Admin;


import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/auth/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminModel loginRequest) {
        try {
            String loginResponse = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(path = "/verify/{id}")
    public ResponseEntity<?> verifyPerson(@PathVariable int id){
        try{
            DeliveryPersonModel verifiedPerson = adminService.verifiedPerson(id);
            return new ResponseEntity<>(verifiedPerson,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(path = "/verifyStore/{id}")
    public ResponseEntity<?> verifyStore(@PathVariable int id){
        try{
            MedicalStoreModel verifiedStore = adminService.verifiedStore(id);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
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
    @GetMapping("/allStores")
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
            return new ResponseEntity<List<DeliveryPersonModel>>(deliverypersons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(path = "/verify/{id}")
    public ResponseEntity<?> verifyPerson(@PathVariable int id){
        try{
            DeliveryPersonModel verifiedPerson = adminService.verifiedPerson(id);
            return new ResponseEntity<>(verifiedPerson,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(path = "/verifyStore/{storeId}")
    public ResponseEntity<?> verifyStore(@PathVariable int storeId){
        try{
            MedicalStoreModel verifiedStore = adminService.verifiedStore(storeId);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path = "/revokeVerifyStore/{storeId}")
    public ResponseEntity<?> revokeVerifyStore(@PathVariable int storeId){
        try{
            MedicalStoreModel verifiedStore = adminService.revokeVerifiedStore(storeId);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/verifyDeliveryPerson/{id}")
    public ResponseEntity<?> verifyDeliveryPerson(@PathVariable int id){
        try{
            DeliveryPersonModel verifyDeliveryPerson = adminService.verifyDeliveryPerson(id);
            return new ResponseEntity<>(verifyDeliveryPerson, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path = "/revokeDeliveryPerson/{id}")
    public ResponseEntity<?> revokeDeliveryPerson(@PathVariable int id){
        try{
            DeliveryPersonModel revokeDeliveryPerson = adminService.revokeDeliveryPerson(id);
            return new ResponseEntity<>(revokeDeliveryPerson, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(path = "/removeDeliveryPerson/{id}")
    public ResponseEntity<?> removeDeliveryPerson(@PathVariable int id) {
        try {
            adminService.removeDeliveryPerson(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No response body
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(path = "/removeStore/{id}")
    public ResponseEntity<?> removeStore(@PathVariable int id) {
        try {
            adminService.removeStore(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No response body
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
