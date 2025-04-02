package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FarmedListingDTO {
    private Long id;
    private String title;
    private String category;
    private String miniDescription;
    private String detailedDescription;
    private String sellType;
    private Double price;
    private Double startingBid;
    private Integer bidDuration;
    private Integer qty;
    private String landDesk;
    private String status;
    private String deletes;
    private String mainImage;
    private List<String> otherImages;
    private List<Double> bidAmounts;
    private boolean termsAccepted;
    private Integer toCart;
    private Integer watchedItem;
    private LocalDate bidStartedDate;
    private LocalDateTime listingDate;
    private Long userId;

    public FarmedListingDTO(String title, String category, String miniDescription, String detailedDescription, String sellType, Double price, Double startingBid, Integer bidDuration, Integer qty, String landDesk, String status, String deletes, String mainImage, List<String> otherImages, boolean termsAccepted, LocalDate bidStartedDate, LocalDateTime listingDate , Long userId) {
        this.title = title;
        this.category = category;
        this.miniDescription = miniDescription;
        this.detailedDescription = detailedDescription;
        this.sellType = sellType;
        this.price = price;
        this.startingBid = startingBid;
        this.bidDuration = bidDuration;
        this.qty = qty;
        this.landDesk = landDesk;
        this.status = status;
        this.deletes = deletes;
        this.mainImage = mainImage;
        this.otherImages = otherImages;
        this.termsAccepted = termsAccepted;
        this.bidStartedDate = bidStartedDate;
        this.listingDate = listingDate;
        this.userId = userId;
    }
}