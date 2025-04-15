package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddToCartWatchListCountDTO {
    private Long listingId;
    private int cartCount;
    private int watchlistCount;
}
