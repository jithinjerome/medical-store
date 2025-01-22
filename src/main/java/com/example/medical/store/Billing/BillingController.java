package com.example.medical.store.Billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/bill")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping(path = "/generate")
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
}
