package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.InquiryDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Inquiry;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AddToCartServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.EmailService;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.InquiryServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/Inquiry")
@CrossOrigin(origins = "*")
public class InquiryController {

    @Autowired
    private InquiryServiceImpl inquiryService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendInquiry(@RequestBody InquiryDTO dto) {
        Inquiry inquiry = new Inquiry();
        inquiry.setOrderId(dto.getOrderId());
        inquiry.setEmail(dto.getEmail());
        inquiry.setMessage(dto.getMessage());
        inquiry.setResponed("No");
        inquiryService.save(inquiry);
        return ResponseEntity.ok("Inquiry saved successfully!");
    }

    @GetMapping
    public List<InquiryDTO> getAllInquiries() {
        return inquiryService.getAllInquiries();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> markAsResponded(@PathVariable Long id) {
        boolean updated = inquiryService.markAsResponded(id);
        if (updated) {
            return ResponseEntity.ok("Marked as responded.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquiry not found.");
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String orderId = payload.get("orderId");
        String message = payload.get("message");

        try {
            emailService.sendInquiryEmail(email, orderId, message);
            return ResponseEntity.ok("Email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }


}
