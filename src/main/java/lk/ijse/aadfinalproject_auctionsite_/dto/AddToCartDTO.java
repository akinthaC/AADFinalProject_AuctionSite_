package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartDTO {
    private Long id;
    private Long userId;
    private Long ListingItemId;
    private String listingType;
    private Integer quantity;

    public AddToCartDTO(Long userId, Long listingItemId, String listingType, Integer quantity) {
        this.userId = userId;
        ListingItemId = listingItemId;
        this.listingType = listingType;
        this.quantity = quantity;
    }
}
