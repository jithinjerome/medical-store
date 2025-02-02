package com.example.medical.store.DeliveryPerson;

import com.example.medical.store.Admin.AdminModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/auth/delivery-people")
public class DeliveryPersonController {

    @Autowired
    private DeliveryPersonService deliveryPersonService;

    @PostMapping("/register")
    public ResponseEntity<?> deliveryPersonRegister(@RequestBody DeliveryPersonModel deliveryPersonModel) {
        try {
            DeliveryPersonModel registeredDeliveryPerson = deliveryPersonService.registerDeliveryPerson(deliveryPersonModel);
            return new ResponseEntity<>(registeredDeliveryPerson, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> deliveryPersonLogin(@RequestBody DeliveryPersonModel loginRequest) {
        try {
            String loginResponse = deliveryPersonService.deliveryPersonLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/allDeliveryPersons")
    public ResponseEntity<List<DeliveryPersonModel>> allDeliveryPersons(){
        return deliveryPersonService.allDeliveryPersons();
    }

    @GetMapping(path = "/verifiedPersons")
    public ResponseEntity<List<DeliveryPersonModel>> verifeidPersons(){
        return deliveryPersonService.verifiedPersons();
    }

    @GetMapping(path = "/notVerified")
    public ResponseEntity<List<DeliveryPersonModel>> notVerifiedPersons(){
        return deliveryPersonService.notVerifiedPerson();
    }
}
