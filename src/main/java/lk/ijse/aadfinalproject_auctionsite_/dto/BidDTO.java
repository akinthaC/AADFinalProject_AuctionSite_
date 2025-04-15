package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidDTO {
    private Long id;
    private Long userId;
    private Long listingId;
    private String listingType;
    private Double bidAmount;
    private LocalDateTime bidTime;
}
