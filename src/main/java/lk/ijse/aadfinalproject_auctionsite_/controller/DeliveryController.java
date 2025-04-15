package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.DeliveryDto;
import lk.ijse.aadfinalproject_auctionsite_.entity.Delivery;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.DeliveryServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.PurchaseServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.WatchItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/Delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    private PurchaseServiceImpl purchaseService;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping
    public ResponseEntity<?> saveDelivery(@RequestBody DeliveryDto dto) {
        try {
            Delivery saved = deliveryService.saveDelivery(dto);
            return ResponseEntity.ok("✅ Delivery saved with ID: " + saved.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }

    @PostMapping("/Shipment")
    public ResponseEntity<String> confirmShipment(
            @RequestParam("listingID") String listingID,
            @RequestParam("Date") String date,
            @RequestParam("trackingNumber") String trackingNumber,
            @RequestParam("estDeliveryDays") int estDeliveryDays,
            @RequestParam("shipmentImage") MultipartFile shipmentImage,
            @RequestParam("status") String status
    ) {
        try {
            // Optional: Save image to server or database
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            String fileName = shipmentImage.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, shipmentImage.getBytes());

            Delivery delivery = deliveryService.findByPurchaseId(listingID);

            // Save shipment details to your DB (pseudo-code)

            delivery.setId(delivery.getId());
            delivery.setDeliveryAssignedDate(LocalDate.parse(date));
            delivery.setTrackingNumber(trackingNumber);
            delivery.setDateCount(String.valueOf(estDeliveryDays));
            delivery.setPackageImage(fileName);
            delivery.setStatus(status);

            // Save to repository
            deliveryService.save(delivery);

            return ResponseEntity.ok("Shipment confirmed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while processing shipment: " + e.getMessage());
        }
    }


    @PostMapping("/UpdateShipment")
    public ResponseEntity<?> updateShipment(@RequestBody Map<String, String> payload) {
        System.out.println(payload.get("orderId"));
        String orderId = payload.get("orderId");
        deliveryService.updateById(orderId);
        return ResponseEntity.ok().body("Shipment updated");
    }
}
