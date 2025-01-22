package com.example.medical.store.Billing;


import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.Prescription.Prescription;
import com.example.medical.store.Prescription.PrescriptionRepository;
import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    private static final BigDecimal GST = new BigDecimal("0.18");
    private static final BigDecimal DELIVERY_CHARGE = new BigDecimal("50");
    int SCALE =2;

    public Billing generateBill(long prescriptionId,long storeId, List<Map<String, BigDecimal>> medicines) {

        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(prescriptionId);

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
        List<String> medicineDetails = new ArrayList<>();

        //Calculating total medicine price and collect medicine details
        for(Map<String, BigDecimal> medicine : medicines){
            for(Map.Entry<String, BigDecimal> entry : medicine.entrySet()){
                totalMedicinePrice = totalMedicinePrice.add(entry.getValue());
                medicineDetails.add(entry.getKey() + ":" + entry.getValue());
            }
        }

        totalMedicinePrice = totalMedicinePrice.setScale(SCALE, RoundingMode.HALF_UP);

        BigDecimal gst = totalMedicinePrice.multiply(GST).setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal deliveryCharge = "Home Delivery".equalsIgnoreCase(prescription.getDeliveryType()) ? DELIVERY_CHARGE.setScale(SCALE, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        BigDecimal totalCharges = totalMedicinePrice.add(gst).add(deliveryCharge).setScale(SCALE, RoundingMode.HALF_UP);

        //Save billing record
        Billing billing = new Billing();
        billing.setUserId(prescription.getUserId());
        billing.setPrescriptionId(prescriptionId);
        billing.setStoreId((int) storeId);
        billing.setStoreName(storeName);
        billing.setDeliveryType(prescription.getDeliveryType());
        billing.setMedicines(medicineDetails);
        billing.setTotalMedicinePrice(totalMedicinePrice);
        billing.setGst(gst);
        billing.setDeliveryCharges(deliveryCharge);
        billing.setTotalCharges(totalCharges);

        return billingRepository.save(billing);

    }
}
