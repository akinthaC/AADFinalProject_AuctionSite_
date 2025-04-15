package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.AllListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.BidDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.PlaceBid;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.PlaceOrderServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.WatchItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/PlaceOrder")
@CrossOrigin(origins = "*")
public class PlaceBidController {
    @Autowired
    private PlaceOrderServiceImpl placeOrderService;

    @Autowired
    private UserServiceImpl userService;


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



}
