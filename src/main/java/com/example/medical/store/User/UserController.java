package com.example.medical.store.User;

import com.example.medical.store.MedicalStore.MedicalStoreModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password){
        return userService.loginUser(email, password);
    }

    @GetMapping("/user-location")
    public ResponseEntity<List<MedicalStoreModel>> findNearByStores(@RequestParam double latitude, @RequestParam double longitude){
        List<MedicalStoreModel> nearByStores =  userService.findNearByStores(latitude,longitude,5.0);
        return ResponseEntity.ok(nearByStores);
    }

    @GetMapping(path = "/allUsers")
    public ResponseEntity<List<User>> allUsers(){
        return userService.allUsers();
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        try{
            userService.sendOtp(email);
            return ResponseEntity.ok("OTP send to your email");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String otp, @RequestParam String newPassword){
        try{
            userService.resetPassword(otp, newPassword);
            return ResponseEntity.ok("Password Reset Successfully");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
