package com.example.medical.store.Billing;


import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.NotificationSystem.Notification.NotificationService;
import com.example.medical.store.Payment.PaymentService;
import com.example.medical.store.Payment.PaymentStatus;
import com.example.medical.store.Prescription.Prescription;
import com.example.medical.store.Prescription.PrescriptionRepository;
import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestRepository;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.razorpay.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.math.RoundingMode;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillingMedicineRepository billingMedicineRepository;

    @Autowired
    private NotificationService notificationService;


    private static final BigDecimal GST = new BigDecimal("0.18");
    private static final BigDecimal DELIVERY_CHARGE = new BigDecimal("50");
    private static final int SCALE =2;

    public Billing generateBill(long prescriptionId,long storeId, List<MedicineItemDTO> medicines) throws RazorpayException {

        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(prescriptionId);

        if (medicines == null || medicines.isEmpty()) {
            throw new IllegalArgumentException("Medicine list cannot be empty");
        }

        if(prescriptionOptional.isEmpty()){
            throw new RuntimeException("Prescription not found");
        }
        Prescription prescription = prescriptionOptional.get();

        Optional<PrescriptionRequest> prescriptionRequestOptional = prescriptionRequestRepository.findByPrescriptionIdAndStoreId(prescriptionId, storeId);
        if(prescriptionRequestOptional.isEmpty()){
            throw new RuntimeException("Prescription Request not found");
        }
        PrescriptionRequest prescriptionRequest = prescriptionRequestOptional.get();

        //Fetch store name
        String storeName = medicalStoreRepo.findById((int) storeId)
                .map(MedicalStoreModel:: getStoreName )
                .orElseThrow(() -> new RuntimeException("Store not found"));

        BigDecimal totalMedicinePrice = BigDecimal.ZERO;
        for(MedicineItemDTO item: medicines){
            totalMedicinePrice = totalMedicinePrice.add(item.getPrice());
        }

        totalMedicinePrice = totalMedicinePrice.setScale(SCALE, RoundingMode.HALF_UP);

        BigDecimal gst = totalMedicinePrice.multiply(GST).setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal deliveryCharge = "Home".equalsIgnoreCase(prescription.getDeliveryType()) ? DELIVERY_CHARGE.setScale(SCALE, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal totalCharges = totalMedicinePrice.add(gst).add(deliveryCharge).setScale(SCALE, RoundingMode.HALF_UP);


        Order razorpayOrder = paymentService.createOrder(totalCharges,"receipt_"+prescriptionId);

        //Save billing record
        Billing billing = new Billing();
        billing.setUserId(prescription.getUserId());
        billing.setPrescriptionId(prescriptionId);
        billing.setStoreId((int) storeId);
        billing.setStoreName(storeName);
        billing.setDeliveryType(prescription.getDeliveryType());
        //billing.setMedicines(medicineDetails);
        billing.setTotalMedicinePrice(totalMedicinePrice);
        billing.setGst(gst);
        billing.setCreatedAt(LocalDateTime.now());
        billing.setDeliveryCharges(deliveryCharge);
        billing.setTotalCharges(totalCharges);
        billing.setRazorpayOrderId(razorpayOrder.get("id"));
        billing.setPaymentStatus(PaymentStatus.PENDING);

        billingRepository.save(billing);
        notificationService.notifyUser(billing.getUserId(), "Your bill has been generated successfully. You can now proceed to review and complete the payment.", "BILL GENERATED");


        for(MedicineItemDTO item: medicines){
            BillingMedicine bm = new BillingMedicine();
            bm.setBillingId(billing.getId());
            bm.setName(item.getName());
            bm.setPrice(item.getPrice());
            billingMedicineRepository.save(bm);
        }
        return billing;

    }

    public List billingByUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            return new ArrayList<>();
        }
        User user = userOptional.get();
        return billingRepository.findByUserId(userId);
    }
}
