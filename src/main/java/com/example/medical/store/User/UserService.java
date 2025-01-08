package com.example.medical.store.User;

import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;


    public ResponseEntity<?> registerUser(User user) {

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if(userOptional.isPresent()){
            throw new IllegalArgumentException("User already exist with same email ID");
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return  new ResponseEntity<>(user,HttpStatus.CREATED);

    }

    public ResponseEntity<?> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials: Password mismatch");
            }
        }
        return ResponseEntity.badRequest().body("Invalid credentials: User not found");
    }

    public ResponseEntity<?> userLocation(String latitude, String longitude) {
        String locationMessage = "User location saved: Latitude " + latitude + ", Longitude " + longitude;
        return new ResponseEntity<>(locationMessage, HttpStatus.OK);
    }
}
