package com.example.medical.store.User;

import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<?> registerUser(User user) {

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if(userOptional.isPresent()){
            return new ResponseEntity<>("User already exists",HttpStatus.CONFLICT);
        }

        User saved = userRepository.save(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> loginUser(String email, String password) {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            return new ResponseEntity<>("Login Succesfull",HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> userLocation(String latitude, String longitude) {
        String locationMessage = "User location saved: Latitude " + latitude + ", Longitude " + longitude;
        return new ResponseEntity<>(locationMessage, HttpStatus.OK);
    }
}
