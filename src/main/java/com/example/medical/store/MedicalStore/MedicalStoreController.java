package com.example.medical.store.MedicalStore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicalStoreController {

    @GetMapping("/medicines")
    public String getAllMedicines(){
        return "All Medicines";
    }
    @GetMapping("/medicine/{id}")
    public String getMedicineById(@PathVariable int id){
        return "Medicine id : "+ id;
    }
}
