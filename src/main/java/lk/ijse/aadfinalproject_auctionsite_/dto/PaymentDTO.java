package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    private String purchaseId;
    private Double amount;
    private String email;
    private String paymentStatus;
    private String paymentMethod;
    private Long listingId;
    private String listingType;
}
