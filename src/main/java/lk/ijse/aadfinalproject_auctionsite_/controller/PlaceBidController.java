package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.*;
import lk.ijse.aadfinalproject_auctionsite_.entity.PlaceBid;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.EmailService;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.PlaceOrderServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.WatchItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/PlaceOrder")
@CrossOrigin(origins = "*")
public class PlaceBidController {
    @Autowired
    private PlaceOrderServiceImpl placeOrderService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailService emailService;


    @PostMapping
    public String placeBid(@RequestParam("userEmail") String userEmail,
                           @RequestParam("listingId") Long listingId,
                           @RequestParam("bidAmount") Double bidAmount,
                           @RequestParam("listingType") String listingType) {

        // Retrieve the user by email
        UserDTO user = userService.getUserByEmail(userEmail);
        if (user == null) {
            return "User not found.";
        }

        // Create a new PlaceBid entity
        BidDTO placeBid = new BidDTO();
        placeBid.setUserId(user.getId());
        placeBid.setListingId(listingId);
        placeBid.setListingType(listingType);
        placeBid.setBidAmount(bidAmount);
        placeBid.setBidTime(LocalDateTime.now());

        // Save the bid
        placeOrderService.savePlaceBid(placeBid);

        // Return a success message
        return "Bid placed successfully!";
    }


    @GetMapping("/{listingId}")
    public ResponseEntity<ResponseDTO> getBidsByListingId(@PathVariable Long listingId) {
        ResponseDTO response = placeOrderService.getBidsByListingId(listingId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("getAllWin/{email}")
    public ResponseEntity<ResponseDTO> getOrdersByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        ResponseDTO response  = placeOrderService.getBidsByUserId(userDTO.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("getPending&Shipped/{email}")
    public ResponseEntity<ResponseDTO> getPendingItemByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        ResponseDTO response  = placeOrderService.getPendingItems(userDTO.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("getPaid/{email}")
    public ResponseEntity<ResponseDTO> getPaidItemByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        ResponseDTO response  = placeOrderService.getPaidItems(userDTO.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("shipped/{email}")
    public ResponseEntity<ResponseDTO> getShippedItemByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        ResponseDTO response  = placeOrderService.getShippedItems(userDTO.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("Delivered/{email}")
    public ResponseEntity<ResponseDTO> getDeliveredItemByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        ResponseDTO response  = placeOrderService.getDeliveredItems(userDTO.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("getAll")
    public ResponseEntity<ResponseDTO> getAllItems() {

        ResponseDTO response  = placeOrderService.getAllItems();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{purchaseId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String purchaseId) {
        try {
            OrderDetailsDTO orderDetails = placeOrderService.getOrderDetailsByPurchaseId(purchaseId);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching order details: " + e.getMessage());
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String orderId = payload.get("orderId");
        String message = payload.get("message");

        try {
            emailService.sendSellerEmail(email, orderId, message);
            return ResponseEntity.ok("Email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }


}




