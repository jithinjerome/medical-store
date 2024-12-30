package com.example.medical.store.DeliveryPerson;

import com.example.medical.store.Admin.AdminModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryPersonService {

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    public DeliveryPersonModel registerDeliveryPerson(DeliveryPersonModel deliveryPersonModel) {
        Optional<DeliveryPersonModel> existingDeliveryPerson =
                deliveryPersonRepo.findByEmail(deliveryPersonModel.getEmail());

        if (existingDeliveryPerson.isPresent()) {
            throw new IllegalArgumentException("Delivery person with this email already exists");
        }
        if (deliveryPersonModel.getVerificationStatus() == null) {
            deliveryPersonModel.setVerificationStatus(DeliveryPersonModel.VerificationStatus.NOT_VERIFIED);
        }
        return deliveryPersonRepo.save(deliveryPersonModel);

    }

    public String deliveryPersonLogin(@Email(message = "Invalid email address") @NotBlank(message = "Email is required") String email, @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        Optional<DeliveryPersonModel> deliveryPerson = deliveryPersonRepo.findByEmail(email);
        if (deliveryPerson.isEmpty() || !deliveryPerson.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return "Delivery Person logged in successfully";
    }
}
