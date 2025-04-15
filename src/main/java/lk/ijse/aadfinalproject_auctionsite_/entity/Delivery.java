package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address1;
    private String address2;

    private String contactNumber;

    private String postalCode;
    private String trackingNumber;

    private String status; // e.g., "Pending", "Shipped", "Delivered"

    private LocalDate deliveryAssignedDate;
    private  String dateCount;
    private String packageImage;


    @OneToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;

}