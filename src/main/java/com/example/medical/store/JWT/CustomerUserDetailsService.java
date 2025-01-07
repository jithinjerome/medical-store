package com.example.medical.store.JWT;

import com.example.medical.store.Admin.AdminModel;
import com.example.medical.store.Admin.AdminRepo;
import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.DeliveryPerson.DeliveryPersonRepo;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities("ROLE_" + user.getRole().name())
                    .build();
        }

        Optional<DeliveryPersonModel> deliveryPersonModelOptional = deliveryPersonRepo.findByEmail(email);
        if(deliveryPersonModelOptional.isPresent()){
            DeliveryPersonModel deliveryPersonModel = deliveryPersonModelOptional.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(deliveryPersonModel.getEmail())
                    .password(deliveryPersonModel.getPassword())
                    .authorities("ROLE_" + deliveryPersonModel.getRole().name())
                    .build();
        }

        Optional<AdminModel> adminOptional = adminRepo.findByEmail(email);
        if(adminOptional.isPresent()){
            AdminModel admin = adminOptional.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(admin.getEmail())
                    .password(admin.getPassword())
                    .authorities("ROLE_" + admin.getRole().name())
                    .build();

        }

        Optional<MedicalStoreModel> medicalStoreOptional = medicalStoreRepo.findByEmail(email);
        if(medicalStoreOptional.isPresent()){
            MedicalStoreModel medicalStore = medicalStoreOptional.get();
            return org.springframework.security.core.userdetails.User
                    .withUsername(medicalStore.getStoreName())
                    .password(medicalStore.getPassword())
                    .authorities("ROLE_" + medicalStore.getRole().name())
                    .build();
        }


        throw new UsernameNotFoundException("User not found with email " + email);
    }
}
