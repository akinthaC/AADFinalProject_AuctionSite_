package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllListingDTO {
    private Long listingID;
    private String listingName;
    private Double price;
    private Double qty;
    private int dateHave;
    private String MainImage;
    private String status;
    private String OrderId;
}
