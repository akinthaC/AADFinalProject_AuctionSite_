package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.PurchaseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.PurchaseServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/Purchase")
@CrossOrigin(origins = "*")
public class PurchaseController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PurchaseServiceImpl purchaseService;


    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PurchaseDTO purchaseDto,
                                        @RequestParam("email") String email) {

        UserDTO user = userService.getUserByEmail(email);

        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setUserId(user.getId());
        purchase.setListingId(purchaseDto.getListingId());
        purchase.setListingType(purchaseDto.getListingType());
        purchase.setQuantity(purchaseDto.getQuantity());
        purchase.setTotalPrice(purchaseDto.getTotalPrice());
        purchase.setPurchaseDate(LocalDateTime.now());

        purchaseService.save(purchase);



        return ResponseEntity.ok("Purchase placed successfully!");
    }

    @GetMapping("/latestPurchaseId")
    public String getLatestPurchaseId() {
        // Fetch the latest purchase ID from the service
        return purchaseService.getLatestPurchaseId();
    }

    @GetMapping("/orders-by-date")
    public List<Map<String, Object>> getOrdersByDate() {
        return purchaseService.getOrdersByDate();
    }

}

