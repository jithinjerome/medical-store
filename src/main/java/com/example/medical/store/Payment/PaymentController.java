package com.example.medical.store.Payment;

import com.example.medical.store.Billing.Billing;
import com.example.medical.store.Billing.BillingRepository;
import com.example.medical.store.NotificationSystem.Notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BillingRepository billingRepository;

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @GetMapping("/key")
    public ResponseEntity<String> getRazorpayKey() {
        logger.info("Fetching Razorpay Key");
        return ResponseEntity.ok(razorpayKeyId);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> payload) {
        String razorpayOrderId = payload.get("razorpay_order_id");
        String razorpayPaymentId = payload.get("razorpay_payment_id");
        String razorpaySignature = payload.get("razorpay_signature");

        if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
            logger.error("Missing required payment details in the payload: razorpay_order_id, razorpay_payment_id, razorpay_signature");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required payment details.");
        }

        try{
            boolean isValid = paymentService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

            Billing billing = billingRepository.findByRazorpayOrderId(razorpayOrderId);

            if (billing == null) {
                logger.error("Order not found for Razorpay Order ID: {}", razorpayOrderId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found in the system.");
            }

            if (!isValid) {
                logger.warn("Invalid payment signature for Razorpay Order ID: {}", razorpayOrderId);
                notificationService.notifyUser(billing.getUserId(), "Your payment could not be processed. Please try again or use a different payment method.", "PAYMENT FAILED");
                billing.setPaymentStatus(PaymentStatus.FAILED);
                billingRepository.save(billing);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment signature");
            }

            logger.info("Payment verified for Razorpay Order ID: {}", razorpayOrderId);
            notificationService.notifyUser(billing.getUserId(), "Your payment has been successfully processed. Thank you for your purchase!", "PAYMENT SUCCESS");

            billing.setPaymentStatus(PaymentStatus.PAID);
            billing.setRazorpayPaymentId(razorpayPaymentId);
            billingRepository.save(billing);
            return ResponseEntity.ok("Payment verified and status updated.");

        } catch (Exception e) {
            logger.error("Error during payment verification for Razorpay Order ID: {}", razorpayOrderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during payment verification.");
        }




    }
}
