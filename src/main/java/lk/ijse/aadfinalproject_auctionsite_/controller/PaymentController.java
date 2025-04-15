package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.PaymentDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Payment;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.PaymentServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.PurchaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PurchaseServiceImpl purchaseService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(@RequestParam String order_id, @RequestParam String payment_id) {
        // Handle the successful payment here
        // You can query PayHere API to verify the payment
        return ResponseEntity.ok("Payment was successful for Order ID: " + order_id);
    }

    @GetMapping("/payment-cancel")
    public ResponseEntity<String> paymentCancel(@RequestParam String order_id) {
        // Handle the canceled payment here
        return ResponseEntity.ok("Payment was canceled for Order ID: " + order_id);
    }

    @GetMapping("/notify")
    public ResponseEntity<String> handlePaymentNotify(@RequestBody String notifyPayload) {
        // Handle PayHere payment notification here (this can be used to confirm payment status)
        return ResponseEntity.ok("Payment notification received.");
    }

    @PostMapping
    public ResponseEntity<?> savePayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            paymentService.savePayment(paymentDTO);
            return ResponseEntity.ok("✅ Payment saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Error saving payment: " + e.getMessage());
        }
    }


}
