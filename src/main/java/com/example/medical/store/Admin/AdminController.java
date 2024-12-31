package com.example.medical.store.Admin;


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
    public ResponseEntity<?> adminRegister(@RequestBody AdminModel adminModel) {
        try {
            AdminModel registeredAdmin = adminService.registerAdmin(adminModel);
            return new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminModel loginRequest) {
        try {
            String loginResponse = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
