package com.example.medical.store.JWT;


import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public AuthController(JWTUtil jwtUtil, UserRepository userRepository,UserDetailsService userDetailsService){
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(path = "/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        String userName = jwtUtil.extractUsernameFromRefreshToken(refreshToken);

        if (userName == null) {
            return ResponseEntity.status(403).body("Invalid Refresh Token: Unable to extract username");
        }

        System.out.println("Extracted Username: " + userName);

        Optional<User> userOptional = userRepository.findByEmail(userName);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(403).body("Invalid Refresh Token: User not found");
        }

        User user = userOptional.get();

        if (!jwtUtil.validateRefreshToken(refreshToken, user.getEmail())) {
            System.out.println("Refresh Token Validation Failed");
            return ResponseEntity.status(403).body("Invalid Refresh Token");
        }

        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
    }

}
