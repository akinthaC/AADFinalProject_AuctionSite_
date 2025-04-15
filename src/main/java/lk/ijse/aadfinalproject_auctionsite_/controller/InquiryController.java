package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.InquiryDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Inquiry;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AddToCartServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.InquiryServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Inquiry")
@CrossOrigin(origins = "*")
public class InquiryController {

    @Autowired
    private InquiryServiceImpl inquiryService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendInquiry(@RequestBody InquiryDTO dto) {
        Inquiry inquiry = new Inquiry();
        inquiry.setOrderId(dto.getOrderId());
        inquiry.setEmail(dto.getEmail());
        inquiry.setMessage(dto.getMessage());
        // inquiryDate is set automatically via @PrePersist
        inquiryService.save(inquiry);
        return ResponseEntity.ok("Inquiry saved successfully!");
    }

}
