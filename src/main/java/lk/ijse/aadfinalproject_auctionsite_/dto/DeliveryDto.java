package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {

    private Long id;
    private String address1;
    private String address2;
    private String contactNumber;
    private String postalCode;
    private String status; // e.g., "Pending", "Shipped", "Delivered"
    private LocalDate deliveryAssignedDate;
    private String dateCount;
    private String packageImage;
    private String purchaseId;
    private String trackingNumber;// Instead of embedding the whole Purchase object, reference by ID

    // Constructors



}