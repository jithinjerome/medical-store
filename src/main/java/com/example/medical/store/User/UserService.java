package com.example.medical.store.User;

import com.example.medical.store.JWT.AuthResponse;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;


    public ResponseEntity<?> registerUser(User user) {

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if(userOptional.isPresent()){
            return new ResponseEntity<>("User already exist with same email ID",HttpStatus.BAD_REQUEST);    
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
                String accessToken = jwtUtil.generateToken(user.getId(),user.getEmail(), user.getRole().name());
                String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
                return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken));
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials: Password mismatch");
            }
        }
        return ResponseEntity.badRequest().body("Invalid credentials: User not found");
    }


    public List<MedicalStoreModel> findNearByStores(double latitude, double longitude, double radiusInKm) {
        List<MedicalStoreModel> allStores = medicalStoreRepo.findByVerificationStatus(VerificationStatus.VERIFIED);
        List<MedicalStoreModel> nearByStores = new ArrayList<>();

        for(MedicalStoreModel store: allStores){
            if(store.getLatitude() != null && store.getLongitude() != null){
                double distance = calculateDistance(latitude,longitude,store.getLatitude(),store.getLongitude());
                if(distance <= radiusInKm){
                    nearByStores.add(store);
                }
            }
        }
        return nearByStores;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2){
        final int EARTH_RADIUS =6371;
        double latDistance = Math.toRadians(lat2-lat1);
        double lonDistance = Math.toRadians(lon2-lon1);
        double a = Math.sin(latDistance/2)*Math.sin(latDistance/2)
                + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                *Math.sin(lonDistance/2)*Math.sin(lonDistance/2);
        double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return EARTH_RADIUS * c;
    }

    public ResponseEntity<List<User>> allUsers() {
        List users = userRepository.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public void sendOtp(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found with the email");
        }

        User user = userOptional.get();
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        sendOtpEmail(email, otp);
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for reset is : " + otp);
        javaMailSender.send(message);
    }

    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    public void resetPassword(String otp, String newPassword) {
        Optional<User> userOptional = userRepository.findByOtp(otp);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("Invalid OTP");
        }
        User user = userOptional.get();
        if(user.getOtpExpiration().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("OTP has Expired");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        user.setOtp(null);
        user.setOtpExpiration(null);
        userRepository.save(user);
    }

    public ResponseEntity<?> getUsers(long id) {
        Optional<User> userOptional= userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public void verifyOTP(String otp) {
        Optional<User> userOptional = userRepository.findByOtp(otp);
        if(userOptional.isEmpty()){
            throw new IllegalArgumentException("Invalid OTP");
        }
        User user = userOptional.get();
        if(user.getOtpExpiration().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("OTP expired");
        }
    }

    public ResponseEntity<?> updateDetails(long id,User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){

            Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
            if(existingEmail.isPresent() && existingEmail.get().getId() != id){
                return new ResponseEntity<>("Email is already in use by another user",HttpStatus.BAD_REQUEST);
            }

            User updatedUser = userOptional.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setAddress(user.getAddress());
            updatedUser.setPhone(user.getPhone());

            userRepository.save(updatedUser);
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }
        throw new RuntimeException("User not found with ID "+id);
    }
}
