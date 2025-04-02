package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
@Entity
public class VehicleListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleType;

    private String make;

    private String yearOfManufacture;

    private String miniDescription;

    private Double mileage;

    private String fuelType;

    private String transmission;

    private String sellingOption;

    private Double price;

    private Double startingBidPrice;

    private Integer bidDuration;

    @Lob
    private String detailedDescription;
    private String mainImage;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> otherImages;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> bidAmounts;

    private boolean termsAccepted;
    private String status;
    private String deletes;
    private Integer toCart;
    private Integer watchedItem;
    private LocalDateTime listingDate;
    private LocalDate bidStartedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @PrePersist
    protected void onCreate() {
        listingDate = LocalDateTime.now();
    }


}
