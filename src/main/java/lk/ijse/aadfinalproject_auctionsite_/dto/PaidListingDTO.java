package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaidListingDTO {
    private String listingId;
    private String listingName;
    private double qty;
    private String status;
    private String date;
    private String userName;
    private String phone;
    private String userId;
    private String mainImage;
    private String address1;
    private String address2;
    private String postalCode;
}
