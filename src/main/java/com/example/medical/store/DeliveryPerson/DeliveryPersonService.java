package com.example.medical.store.DeliveryPerson;

import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DeliveryPersonService {

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public DeliveryPersonModel registerDeliveryPerson(DeliveryPersonModel deliveryPersonModel) {
        Optional<DeliveryPersonModel> existingDeliveryPerson =
                deliveryPersonRepo.findByEmail(deliveryPersonModel.getEmail());

        if (existingDeliveryPerson.isPresent()) {
            throw new IllegalArgumentException("Delivery person with this email already exists");
        }
        if (deliveryPersonModel.getVerificationStatus() == null) {
            deliveryPersonModel.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
        }
        if (deliveryPersonModel.getRole() == null) {
            deliveryPersonModel.setRole(Role.DELIVERYPERSON);
        }
        deliveryPersonModel.setPassword(passwordEncoder.encode(deliveryPersonModel.getPassword()));

        return deliveryPersonRepo.save(deliveryPersonModel);

    }

    public String deliveryPersonLogin(@Email(message = "Invalid email address") @NotBlank(message = "Email is required") String email, @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        Optional<DeliveryPersonModel> deliveryPersonOptional = deliveryPersonRepo.findByEmail(email);

        if(deliveryPersonOptional.isPresent()){
            DeliveryPersonModel deliveryPerson = deliveryPersonOptional.get();
            if(passwordEncoder.matches(password, deliveryPerson.getPassword())){
                return jwtUtil.generateToken(deliveryPerson.getEmail(),deliveryPerson.getRole().name());

            }else {
                throw new IllegalArgumentException("Invalid credentials: Password mismatch");
            }
        }
        throw new IllegalArgumentException("Invalid credentials: User not found");
    }

    public ResponseEntity<List<DeliveryPersonModel>> allDeliveryPersons() {
        List<DeliveryPersonModel> deliveryPersons = deliveryPersonRepo.findAll();
        return new ResponseEntity<>(deliveryPersons, HttpStatus.OK);
    }
}
