package com.example.medical.store.Billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/bill")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping(path = "/generate")
    @PreAuthorize("hasRole('MEDICAL_STORE')")
    public ResponseEntity<?> generateBill(@RequestParam long prescriptionId,
                                          @RequestParam long storeId,
                                          @RequestBody List<Map<String, BigDecimal>> medicines){
        try {
            Billing bill = billingService.generateBill(prescriptionId,storeId,medicines);
            return ResponseEntity.ok(bill);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<List<Billing>> billingByUser(@PathVariable long userId){
        List user = billingService.billingByUser(userId);

        if(user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


}
