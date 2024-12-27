package com.example.medical.store.Admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;



    @PostMapping("/register")
    public ResponseEntity<?> adminRegister(@Valid @RequestBody AdminModel adminModel) throws IOException {
        AdminModel registeredAdmin = adminService.adminCredentials(adminModel);

        return new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@Valid @RequestParam String email, @RequestParam String password) {

        try{
            String loginResponse = adminService.adminLogin(email,password);
            return new ResponseEntity<>(loginResponse,HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);

        }
    }
}
