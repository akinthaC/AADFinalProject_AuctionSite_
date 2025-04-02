package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class LandListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String landName;
    private String location;
    private String currentUse;
    private String size;
    private String topography;
    private String waterAvailability;
    private String access;
    private String soilQuality;
    private String features;
    @Lob
    private String detailedDescription;

    private Double price;
    private Double startPrice;
    private Double reservePrice;
    private Integer auctionDuration;

    private String paymentTerms;
    private LocalDate auctionStartDate;

    private String mainImage;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> otherImages;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> bidAmounts;


    private LocalDateTime listingDate;

    private Integer toCart;
    private Integer watchedItem;
    private String status;
    private String deletes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @PrePersist
    protected void onCreate() {listingDate = LocalDateTime.now();}
}
