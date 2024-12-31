package com.example.medical.store.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
