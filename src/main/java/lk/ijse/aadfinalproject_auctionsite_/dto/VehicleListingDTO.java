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
public class VehicleListingDTO {

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
    private String detailedDescription;
    private String mainImage;
    private List<String> otherImages;
    private List<Double> bidAmounts;
    private boolean termsAccepted;
    private LocalDate bidStartedDate;
    private Integer toCart;
    private Integer watchedItem;
    private LocalDateTime listingDate;
    private String status;
    private String deletes;
    private Long userId;

    public VehicleListingDTO(String vehicleType, String make, String yearOfManufacture, String miniDescription, Double mileage, String fuelType, String transmission, String sellingOption, Double price, Double startingBidPrice, Integer bidDuration, String detailedDescription, String mainImage, List<String> otherImages, boolean termsAccepted, LocalDateTime listingDate, String status, String deletes, Long userId, LocalDate bidStartedDate) {
        this.vehicleType = vehicleType;
        this.make = make;
        this.yearOfManufacture = yearOfManufacture;
        this.miniDescription = miniDescription;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.sellingOption = sellingOption;
        this.price = price;
        this.startingBidPrice = startingBidPrice;
        this.bidDuration = bidDuration;
        this.detailedDescription = detailedDescription;
        this.mainImage = mainImage;
        this.otherImages = otherImages;
        this.termsAccepted = termsAccepted;
        this.listingDate = listingDate;
        this.status = status;
        this.deletes = deletes;
        this.userId = userId;
        this.bidStartedDate = bidStartedDate;
    }
}
