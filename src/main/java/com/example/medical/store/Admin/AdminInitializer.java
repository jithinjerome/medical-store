package com.example.medical.store.Admin;

import com.example.medical.store.User.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.medical.store.User.Role.ADMIN;
@Component
public class AdminInitializer {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeAdmin(){
        if(adminRepo.count()==0){
            String defaultEmail = "admin@123";
            String defaultPassword = "admin@123";
            Role defaultRole = Role.ADMIN;

            AdminModel admin = new AdminModel();
            admin.setEmail(defaultEmail);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setRole(defaultRole);
            adminRepo.save(admin);

            System.out.println("Admin Created");
            System.out.println("Email: " + admin.getEmail());
            System.out.println("Password : " + admin.getPassword());
        }else{
            System.out.println("Admin already exist. No default Admin is created");
        }
    }
}
