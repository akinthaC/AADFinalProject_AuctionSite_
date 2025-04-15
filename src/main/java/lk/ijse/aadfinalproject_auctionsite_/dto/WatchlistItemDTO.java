package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistItemDTO {
    private Long id;
    private Long userId;
    private Long ListingItemId;
    private String listingType;

    public WatchlistItemDTO(Long userId, Long ListingItemId, String listingType) {
        this.userId = userId;
        this.ListingItemId = ListingItemId;
        this.listingType = listingType;
    }
}
