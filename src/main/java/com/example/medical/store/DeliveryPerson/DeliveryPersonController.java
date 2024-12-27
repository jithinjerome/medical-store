package com.example.medical.store.DeliveryPerson;

import com.example.medical.store.Admin.AdminModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth/deliveryperson")
public class DeliveryPersonController {

    @PostMapping("/register")
    public ResponseEntity<String> deliveryPersonRegister(@Valid @RequestBody DeliveryPersonModel deliveryPersonModel) {
        return ResponseEntity.ok("Delivery Person registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<String> deliveryPersonLogin(@Valid @RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok("Delivery Person logged in successfully");
    }
}
