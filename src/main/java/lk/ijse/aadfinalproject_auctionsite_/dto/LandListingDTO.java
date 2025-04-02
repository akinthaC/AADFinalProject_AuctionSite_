package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LandListingDTO {
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
    private String detailedDescription;

    private Double price;
    private Double startPrice;
    private Double reservePrice;
    private Integer auctionDuration;

    private String paymentTerms;
    private LocalDate auctionStartDate;

    private String mainImage;
    private List<String> otherImages;
    private List<Double> bidAmounts;

    private LocalDateTime listingDate;
    private Integer toCart;
    private Integer watchedItem;
    private String status;
    private String deletes;
    private Long userId;

    public LandListingDTO(String landName, String location, String currentUse, String size, String topography, String waterAvailability, String access, String soilQuality, String features, String detailedDescription, Double price, Double startPrice, Double reservePrice, Integer auctionDuration, String paymentTerms, LocalDate auctionStartDate, String mainImage, List<String> otherImages, LocalDateTime listingDate, String status, String deletes,Long userId) {
        this.landName = landName;
        this.location = location;
        this.currentUse = currentUse;
        this.size = size;
        this.topography = topography;
        this.waterAvailability = waterAvailability;
        this.access = access;
        this.soilQuality = soilQuality;
        this.features = features;
        this.detailedDescription = detailedDescription;
        this.price = price;
        this.startPrice = startPrice;
        this.reservePrice = reservePrice;
        this.auctionDuration = auctionDuration;
        this.paymentTerms = paymentTerms;
        this.auctionStartDate = auctionStartDate;
        this.mainImage = mainImage;
        this.otherImages = otherImages;
        this.listingDate = listingDate;
        this.status = status;
        this.deletes = deletes;
        this.userId = userId;
    }
}
