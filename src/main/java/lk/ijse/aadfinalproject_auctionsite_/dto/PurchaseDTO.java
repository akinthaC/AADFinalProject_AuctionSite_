package lk.ijse.aadfinalproject_auctionsite_.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private String id;
    private Long userId;
    private Long listingId; // ID of the listing being bid on
    private String listingType;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime purchaseDate;
    private String status;
    private String desc;

    public PurchaseDTO(String id, Long listingId, String listingType, Integer quantity, Double totalPrice, LocalDateTime purchaseDate) {
        this.id = id;
        this.listingId = listingId;
        this.listingType = listingType;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
    }
}
